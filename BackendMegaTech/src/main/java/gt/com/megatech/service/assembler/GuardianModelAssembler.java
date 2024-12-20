package gt.com.megatech.service.assembler;

import gt.com.megatech.presentation.controller.GuardianController;
import gt.com.megatech.presentation.dto.GuardianDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GuardianModelAssembler implements RepresentationModelAssembler<GuardianDTO, EntityModel<GuardianDTO>> {

    @Override
    public @NonNull EntityModel<GuardianDTO> toModel(@NonNull GuardianDTO guardianDTO) {
        return EntityModel.of(
                guardianDTO,
                linkTo(methodOn(GuardianController.class).findGuardianById(guardianDTO.getId())).withSelfRel(),
                linkTo(methodOn(GuardianController.class).findGuardianByIdWithStudents(guardianDTO.getId())).withRel("guardian-with-students"),
                linkTo(methodOn(GuardianController.class).findAllGuardians()).withRel("guardian"),
                linkTo(methodOn(GuardianController.class).findAllGuardiansWithStudents()).withRel("guardians-with-students")
        );
    }
}
