package gt.com.megatech.service.assembler;

import gt.com.megatech.presentation.controller.SuspendedStudentController;
import gt.com.megatech.presentation.dto.SuspendedStudentDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SuspendedStudentModelAssembler implements RepresentationModelAssembler<SuspendedStudentDTO, EntityModel<SuspendedStudentDTO>> {

    @Override
    public @NonNull EntityModel<SuspendedStudentDTO> toModel(
            @NonNull SuspendedStudentDTO suspendedStudentDTO
    ) {
        return EntityModel.of(
                suspendedStudentDTO,
                linkTo(methodOn(SuspendedStudentController.class)
                        .findByIdSuspendedStudent(suspendedStudentDTO.getId()))
                        .withSelfRel(),
                linkTo(methodOn(SuspendedStudentController.class)
                        .findAllSuspendedStudents())
                        .withRel("suspended-student")
        );
    }
}
