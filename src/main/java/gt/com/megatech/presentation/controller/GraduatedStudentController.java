package gt.com.megatech.presentation.controller;

import gt.com.megatech.presentation.dto.GraduatedStudentDTO;
import gt.com.megatech.service.assembler.GraduatedStudentModelAssembler;
import gt.com.megatech.service.interfaces.IGraduatedStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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
        "/api/graduated-student"
)
@RequiredArgsConstructor
@PreAuthorize(
        "denyAll()"
)
public class GraduatedStudentController {

    private final GraduatedStudentModelAssembler graduatedStudentModelAssembler;
    private final PagedResourcesAssembler<GraduatedStudentDTO> graduatedStudentDTOPagedResourcesAssembler;
    private final IGraduatedStudentService iGraduatedStudentService;

    @PreAuthorize(
            "hasAuthority('READ')"
    )
    @GetMapping
    public CollectionModel<EntityModel<GraduatedStudentDTO>> findAllGraduatedStudents() {
        List<EntityModel<GraduatedStudentDTO>> entityModelList = this.iGraduatedStudentService.findAllGraduatedStudents()
                .stream()
                .map(graduatedStudentModelAssembler::toModel)
                .toList();
        return CollectionModel.of(
                entityModelList,
                linkTo(methodOn(GraduatedStudentController.class).findAllGraduatedStudents())
                        .withSelfRel()
        );
    }

    @PreAuthorize(
            "hasAuthority('READ')"
    )
    @GetMapping(
            "/paged"
    )
    public ResponseEntity<PagedModel<EntityModel<GraduatedStudentDTO>>> findAllGraduatedStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Pageable pageable
    ) {
        Pageable customPageable = PageRequest.of(
                page,
                size,
                pageable.getSort()
        );
        Page<GraduatedStudentDTO> graduatedStudentDTOPage = this.iGraduatedStudentService.findAllGraduatedStudents(
                customPageable
        );
        PagedModel<EntityModel<GraduatedStudentDTO>> entityModelPagedModel = this.graduatedStudentDTOPagedResourcesAssembler
                .toModel(
                        graduatedStudentDTOPage,
                        graduatedStudentModelAssembler
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
    public EntityModel<GraduatedStudentDTO> findByIdGraduatedStudent(
            @PathVariable Long id
    ) {
        GraduatedStudentDTO graduatedStudentDTO = this.iGraduatedStudentService.findByIdGraduatedStudent(
                id
        );
        return graduatedStudentModelAssembler.toModel(
                graduatedStudentDTO
        );
    }

    @PreAuthorize(
            "hasAuthority('DELETE')"
    )
    @DeleteMapping(
            "/{id}"
    )
    public ResponseEntity<EntityModel<GraduatedStudentDTO>> deleteGraduatedStudent(
            @PathVariable Long id
    ) {
        this.iGraduatedStudentService.deleteGraduatedStudent(
                id
        );
        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT
        );
    }
}
