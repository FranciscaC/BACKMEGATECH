package gt.com.megatech.service.assembler;

import gt.com.megatech.presentation.controller.GraduatedStudentController;
import gt.com.megatech.presentation.controller.SuspendedStudentController;
import gt.com.megatech.presentation.dto.GraduatedStudentDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GraduatedStudentModelAssembler implements RepresentationModelAssembler<GraduatedStudentDTO, EntityModel<GraduatedStudentDTO>> {

    @Override
    public @NonNull EntityModel<GraduatedStudentDTO> toModel(
            @NonNull GraduatedStudentDTO graduatedStudentDTO
    ) {
        return EntityModel.of(
                graduatedStudentDTO,
                linkTo(methodOn(GraduatedStudentController.class)
                        .findByIdGraduatedStudent(graduatedStudentDTO.getId()))
                        .withSelfRel(),
                linkTo(methodOn(GraduatedStudentController.class)
                        .findAllGraduatedStudents())
                        .withRel("graduated-student")
        );
    }
}
