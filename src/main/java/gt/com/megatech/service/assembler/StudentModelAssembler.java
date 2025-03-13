package gt.com.megatech.service.assembler;

import gt.com.megatech.persistence.entity.enums.AcademicStatusEnum;
import gt.com.megatech.presentation.controller.StudentController;
import gt.com.megatech.presentation.dto.StudentDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class StudentModelAssembler implements RepresentationModelAssembler<StudentDTO, EntityModel<StudentDTO>> {

    @Override
    public @NonNull EntityModel<StudentDTO> toModel(
            @NonNull StudentDTO studentDTO
    ) {
        EntityModel<StudentDTO> studentDTOEntityModel = EntityModel.of(
                studentDTO,
                linkTo(methodOn(StudentController.class)
                        .findByIdStudent(studentDTO.getId()))
                        .withSelfRel(),
                linkTo(methodOn(StudentController.class)
                        .findAllStudyingStudents())
                        .withRel("studying-students"),
                linkTo(methodOn(StudentController.class)
                        .findAllGraduatedStudents())
                        .withRel("graduated-students"),
                linkTo(methodOn(StudentController.class)
                        .findAllSuspendedStudents())
                        .withRel("suspended-students")
        );
        if (studentDTO.getAcademicStatusEnum() == AcademicStatusEnum.STUDYING) {
            studentDTOEntityModel.add(linkTo(methodOn(StudentController.class)
                    .updateAcademicStatusToGraduated(studentDTO.getId()))
                    .withRel("graduate-student")
            );
        }
        if (studentDTO.getAcademicStatusEnum() == AcademicStatusEnum.STUDYING) {
            studentDTOEntityModel.add(linkTo(methodOn(StudentController.class)
                    .updateAcademicStatusToSuspended(studentDTO.getId()))
                    .withRel("suspend-student")
            );
        }
        if (studentDTO.getAcademicStatusEnum() == AcademicStatusEnum.SUSPENDED) {
            studentDTOEntityModel.add(linkTo(methodOn(StudentController.class)
                    .updateAcademicStatusToStudying(studentDTO.getId()))
                    .withRel("studying-student")
            );
        }
        return studentDTOEntityModel;
    }
}
