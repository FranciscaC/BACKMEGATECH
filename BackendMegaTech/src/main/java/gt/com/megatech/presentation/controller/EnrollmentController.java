package gt.com.megatech.presentation.controller;

import gt.com.megatech.presentation.dto.EnrollmentDTO;
import gt.com.megatech.service.assembler.EnrollmentModelAssembler;
import gt.com.megatech.service.interfaces.IEnrollmentService;
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
@RequestMapping("/api/enrollment")
@RequiredArgsConstructor
@PreAuthorize("denyAll()")
public class EnrollmentController {

    private final EnrollmentModelAssembler enrollmentModelAssembler;
    private final PagedResourcesAssembler<EnrollmentDTO> enrollmentDTOPagedResourcesAssembler;
    private final IEnrollmentService iEnrollmentService;

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping
    public CollectionModel<EntityModel<EnrollmentDTO>> findAllEnrollments() {
        List<EntityModel<EnrollmentDTO>> enrollments = this.iEnrollmentService.findAllEnrollments()
                .stream()
                .map(enrollmentModelAssembler::toModel)
                .toList();
        return CollectionModel.of(
                enrollments,
                linkTo(methodOn(EnrollmentController.class).findAllEnrollments()).withSelfRel()
        );
    }

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping("/paged")
    public ResponseEntity<PagedModel<EntityModel<EnrollmentDTO>>> findAllEnrollmentsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Pageable pageable
    ) {
        Pageable customPageable = PageRequest.of(page, size, pageable.getSort());
        Page<EnrollmentDTO> enrollmentDTOPage = this.iEnrollmentService.findAllEnrollmentsPaged(customPageable);
        PagedModel<EntityModel<EnrollmentDTO>> entityModelPagedModel = this.enrollmentDTOPagedResourcesAssembler.toModel(
                enrollmentDTOPage,
                enrollmentModelAssembler
        );
        return ResponseEntity.ok(entityModelPagedModel);
    }

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping("/{id}")
    public EntityModel<EnrollmentDTO> findByIdEnrollment(@PathVariable Long id) {
        EnrollmentDTO enrollment = this.iEnrollmentService.findByIdEnrollment(id);
        return enrollmentModelAssembler.toModel(enrollment);
    }

    @PreAuthorize("hasAuthority('CREATE')")
    @PostMapping
    public ResponseEntity<EntityModel<EnrollmentDTO>> saveEnrollment(@RequestBody @Valid EnrollmentDTO enrollmentDTO) {
        EntityModel<EnrollmentDTO> enrollmentDTOEntityModel = enrollmentModelAssembler
                .toModel(this.iEnrollmentService.saveEnrollment(enrollmentDTO));
        return ResponseEntity
                .created(enrollmentDTOEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(enrollmentDTOEntityModel);
    }

    @PreAuthorize("hasAuthority('UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<EnrollmentDTO>> updateEnrollment(@PathVariable Long id, @RequestBody @Valid EnrollmentDTO enrollmentDTO) {
        EntityModel<EnrollmentDTO> enrollmentDTOEntityModel = enrollmentModelAssembler
                .toModel(this.iEnrollmentService.updateEnrollment(id, enrollmentDTO));
        return ResponseEntity
                .created(enrollmentDTOEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(enrollmentDTOEntityModel);
    }

    @PreAuthorize("hasAuthority('DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        this.iEnrollmentService.deleteEnrollment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
