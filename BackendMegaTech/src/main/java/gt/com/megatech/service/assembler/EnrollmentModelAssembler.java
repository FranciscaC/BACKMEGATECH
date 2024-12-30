package gt.com.megatech.service.assembler;

import gt.com.megatech.presentation.controller.EnrollmentController;
import gt.com.megatech.presentation.dto.EnrollmentDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EnrollmentModelAssembler implements RepresentationModelAssembler<EnrollmentDTO, EntityModel<EnrollmentDTO>> {

    @Override
    public @NonNull EntityModel<EnrollmentDTO> toModel(@NonNull EnrollmentDTO enrollmentDTO) {
        return EntityModel.of(
                enrollmentDTO,
                linkTo(methodOn(EnrollmentController.class).findByIdEnrollment(enrollmentDTO.getId())).withSelfRel(),
                linkTo(methodOn(EnrollmentController.class).findAllEnrollments()).withRel("enrollment")
        );
    }
}
