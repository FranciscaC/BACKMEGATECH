package gt.com.megatech.presentation.controller;

import gt.com.megatech.presentation.dto.StudentDTO;
import gt.com.megatech.service.assembler.StudentModelAssembler;
import gt.com.megatech.service.interfaces.IStudentService;
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
@RequestMapping("/api/student")
@RequiredArgsConstructor
@PreAuthorize("denyAll()")
public class StudentController {

    private final StudentModelAssembler studentModelAssembler;
    private final PagedResourcesAssembler<StudentDTO> studentDTOPagedResourcesAssembler;
    private final IStudentService iStudentService;

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping
    public CollectionModel<EntityModel<StudentDTO>> findAllStudent() {
        List<EntityModel<StudentDTO>> students = this.iStudentService.findAllStudent()
                .stream()
                .map(studentModelAssembler::toModel)
                .toList();
        return CollectionModel.of(
                students,
                linkTo(methodOn(StudentController.class).findAllStudent()).withSelfRel()
        );
    }

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping("/paged")
    public ResponseEntity<PagedModel<EntityModel<StudentDTO>>> findAllStudentPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Pageable pageable
    ) {
        Pageable customPageable = PageRequest.of(page, size, pageable.getSort());
        Page<StudentDTO> studentDTOPage = this.iStudentService.findAllStudentPaged(customPageable);
        PagedModel<EntityModel<StudentDTO>> entityModelPagedModel = studentDTOPagedResourcesAssembler.toModel(
                studentDTOPage,
                studentModelAssembler
        );
        return ResponseEntity.ok(entityModelPagedModel);
    }

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping("/{id}")
    public EntityModel<StudentDTO> findByIdStudent(@PathVariable Long id) {
        StudentDTO student = this.iStudentService.findByIdStudent(id);
        return studentModelAssembler.toModel(student);
    }

    @PreAuthorize("hasAuthority('CREATE')")
    @PostMapping
    public ResponseEntity<EntityModel<StudentDTO>> saveStudent(@RequestBody @Valid StudentDTO studentDTO) {
        EntityModel<StudentDTO> studentDTOEntityModel = studentModelAssembler
                .toModel(this.iStudentService.saveStudent(studentDTO));
        return ResponseEntity
                .created(studentDTOEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(studentDTOEntityModel);
    }

    @PreAuthorize("hasAuthority('UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<StudentDTO>> updateStudent(@PathVariable Long id, @RequestBody @Valid StudentDTO studentDTO) {
        EntityModel<StudentDTO> studentDTOEntityModel = studentModelAssembler
                .toModel(this.iStudentService.updateStudent(id, studentDTO));
        return ResponseEntity
                .created(studentDTOEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(studentDTOEntityModel);
    }

    @PreAuthorize("hasAuthority('DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        this.iStudentService.deleteStudent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
