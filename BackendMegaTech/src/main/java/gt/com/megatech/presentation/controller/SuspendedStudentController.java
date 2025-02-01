package gt.com.megatech.presentation.controller;

import gt.com.megatech.presentation.dto.SuspendedStudentDTO;
import gt.com.megatech.service.assembler.SuspendedStudentModelAssembler;
import gt.com.megatech.service.interfaces.ISuspendedStudentService;
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
        "/api/suspended-student"
)
@RequiredArgsConstructor
@PreAuthorize(
        "denyAll()"
)
public class SuspendedStudentController {

    private final SuspendedStudentModelAssembler suspendedStudentModelAssembler;
    private final PagedResourcesAssembler<SuspendedStudentDTO> suspendedStudentDTOPagedResourcesAssembler;
    private final ISuspendedStudentService iSuspendedStudentService;

    @PreAuthorize(
            "hasAuthority('READ')"
    )
    @GetMapping
    public CollectionModel<EntityModel<SuspendedStudentDTO>> findAllSuspendedStudents() {
        List<EntityModel<SuspendedStudentDTO>> entityModelList = this.iSuspendedStudentService.findAllSuspendedStudents()
                .stream()
                .map(suspendedStudentModelAssembler::toModel)
                .toList();
        return CollectionModel.of(
                entityModelList,
                linkTo(methodOn(SuspendedStudentController.class).findAllSuspendedStudents())
                        .withSelfRel()
        );
    }

    @PreAuthorize(
            "hasAuthority('READ')"
    )
    @GetMapping(
            "/paged"
    )
    public ResponseEntity<PagedModel<EntityModel<SuspendedStudentDTO>>> findAllSuspendedStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Pageable pageable
    ) {
        Pageable customPageable = PageRequest.of(
                page,
                size,
                pageable.getSort()
        );
        Page<SuspendedStudentDTO> suspendedStudentDTOPage = this.iSuspendedStudentService.findAllSuspendedStudents(
                customPageable
        );
        PagedModel<EntityModel<SuspendedStudentDTO>> entityModelPagedModel = this.suspendedStudentDTOPagedResourcesAssembler
                .toModel(
                        suspendedStudentDTOPage,
                        suspendedStudentModelAssembler
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
    public EntityModel<SuspendedStudentDTO> findByIdSuspendedStudent(
            @PathVariable Long id
    ) {
        SuspendedStudentDTO suspendedStudentDTO = this.iSuspendedStudentService.findByIdSuspendedStudent(
                id
        );
        return suspendedStudentModelAssembler.toModel(
                suspendedStudentDTO
        );
    }

    @PreAuthorize(
            "hasAuthority('CREATE')"
    )
    @PostMapping
    public ResponseEntity<EntityModel<SuspendedStudentDTO>> saveSuspendedStudent(
            @RequestBody @Valid SuspendedStudentDTO suspendedStudentDTO
    ) {
        EntityModel<SuspendedStudentDTO> suspendedStudentDTOEntityModel = suspendedStudentModelAssembler
                .toModel(this.iSuspendedStudentService.saveSuspendedStudent(
                        suspendedStudentDTO
                ));
        return ResponseEntity
                .created(suspendedStudentDTOEntityModel.getRequiredLink(
                        IanaLinkRelations.SELF
                ).toUri()).body(suspendedStudentDTOEntityModel);
    }

    @PreAuthorize(
            "hasAuthority('UPDATE')"
    )
    @PutMapping(
            "/{id}"
    )
    public ResponseEntity<EntityModel<SuspendedStudentDTO>> updateSuspendedStudent(
            @PathVariable Long id,
            @RequestBody @Valid SuspendedStudentDTO suspendedStudentDTO
    ) {
        EntityModel<SuspendedStudentDTO> suspendedStudentDTOEntityModel = suspendedStudentModelAssembler
                .toModel(this.iSuspendedStudentService.updateSuspendedStudent(
                        id,
                        suspendedStudentDTO
                ));
        return ResponseEntity
                .created(suspendedStudentDTOEntityModel.getRequiredLink(
                        IanaLinkRelations.SELF
                ).toUri()).body(suspendedStudentDTOEntityModel);
    }

    @PreAuthorize(
            "hasAuthority('DELETE')"
    )
    @DeleteMapping(
            "/{id}"
    )
    public ResponseEntity<EntityModel<SuspendedStudentDTO>> deleteSuspendedStudent(
            @PathVariable Long id
    ) {
        this.iSuspendedStudentService.deleteSuspendedStudent(
                id
        );
        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT
        );
    }
}
