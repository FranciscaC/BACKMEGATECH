package gt.com.megatech.presentation.controller;

import gt.com.megatech.presentation.dto.ProfessorDTO;
import gt.com.megatech.service.assembler.ProfessorModelAssembler;
import gt.com.megatech.service.interfaces.IProfessorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(
        "/api/professor"
)
@RequiredArgsConstructor
@PreAuthorize(
        "denyAll()"
)
public class ProfessorController {

    private final ProfessorModelAssembler professorModelAssembler;
    private final PagedResourcesAssembler<ProfessorDTO> professorDTOPagedResourcesAssembler;
    private final IProfessorService iProfessorService;

    @PreAuthorize(
            "hasAuthority('READ')"
    )
    @GetMapping
    public CollectionModel<EntityModel<ProfessorDTO>> findAllProfessors() {
        List<EntityModel<ProfessorDTO>> entityModelList = this.iProfessorService.findAllProfessors()
                .stream()
                .map(professorModelAssembler::toModel)
                .toList();
        return CollectionModel.of(
                entityModelList,
                linkTo(methodOn(ProfessorController.class).findAllProfessors())
                        .withSelfRel()
        );
    }

    @PreAuthorize(
            "hasAuthority('READ')"
    )
    @GetMapping(
            "/paged"
    )
    public ResponseEntity<PagedModel<EntityModel<ProfessorDTO>>> findAllProfessorsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Pageable pageable
    ) {
        Pageable customPageable = PageRequest.of(
                page,
                size,
                pageable.getSort()
        );
        Page<ProfessorDTO> professorDTOPage = this.iProfessorService.findAllProfessorsPaged(
                customPageable
        );
        PagedModel<EntityModel<ProfessorDTO>> entityModelPagedModel = this.professorDTOPagedResourcesAssembler
                .toModel(
                        professorDTOPage,
                        professorModelAssembler
                );
        return ResponseEntity.ok(
                entityModelPagedModel
        );
    }

    @PreAuthorize(
            "hasAuthority('READ')"
    )
    @GetMapping(
            "/{id}"
    )
    public EntityModel<ProfessorDTO> findByIdProfessor(
            @PathVariable Long id
    ) {
        ProfessorDTO professorDTO = this.iProfessorService.findByIdProfessor(
                id
        );
        return professorModelAssembler.toModel(
                professorDTO
        );
    }

    @PreAuthorize(
            "hasAuthority('CREATE')"
    )
    @PostMapping
    public ResponseEntity<EntityModel<ProfessorDTO>> saveProfessor(
            @RequestBody @Valid ProfessorDTO professorDTO
    ) {
        EntityModel<ProfessorDTO> guardianDTOEntityModel = professorModelAssembler
                .toModel(this.iProfessorService.saveProfessor(
                        professorDTO
                ));
        return ResponseEntity
                .created(guardianDTOEntityModel.getRequiredLink(
                                IanaLinkRelations.SELF
                        )
                        .toUri()).body(guardianDTOEntityModel);
    }

    @PreAuthorize(
            "hasAuthority('UPDATE')"
    )
    @PutMapping(
            "/{id}"
    )
    public ResponseEntity<EntityModel<ProfessorDTO>> updateProfessor(
            @PathVariable Long id,
            @RequestBody @Valid ProfessorDTO professorDTO
    ) {
        EntityModel<ProfessorDTO> guardianDTOEntityModel = professorModelAssembler
                .toModel(this.iProfessorService.updateProfessor(
                        id,
                        professorDTO
                ));
        return ResponseEntity
                .created(guardianDTOEntityModel.getRequiredLink(
                                IanaLinkRelations.SELF
                        )
                        .toUri()).body(guardianDTOEntityModel);
    }

    @PreAuthorize(
            "hasAuthority('DELETE')"
    )
    @DeleteMapping(
            "/{id}"
    )
    public ResponseEntity<Void> deleteProfessor(
            @PathVariable Long id
    ) {
        this.iProfessorService.deleteProfessor(
                id
        );
        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT
        );
    }
}
