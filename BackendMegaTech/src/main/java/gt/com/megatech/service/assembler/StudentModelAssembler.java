package gt.com.megatech.service.assembler;

import gt.com.megatech.presentation.controller.StudentController;
import gt.com.megatech.presentation.dto.StudentDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class StudentModelAssembler implements RepresentationModelAssembler<StudentDTO, EntityModel<StudentDTO>> {

    @Override
    public EntityModel<StudentDTO> toModel(StudentDTO studentDTO) {
        return EntityModel.of(
                studentDTO,
                linkTo(methodOn(StudentController.class).findByIdStudent(studentDTO.getId())).withSelfRel(),
                linkTo(methodOn(StudentController.class).findAllStudent()).withRel("student")
        );
    }
}
