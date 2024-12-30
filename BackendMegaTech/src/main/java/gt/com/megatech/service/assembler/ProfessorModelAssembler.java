package gt.com.megatech.service.assembler;

import gt.com.megatech.presentation.controller.ProfessorController;
import gt.com.megatech.presentation.dto.ProfessorDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProfessorModelAssembler implements RepresentationModelAssembler<ProfessorDTO, EntityModel<ProfessorDTO>> {

    @Override
    public @NonNull EntityModel<ProfessorDTO> toModel(@NonNull ProfessorDTO professorDTO) {
        return EntityModel.of(
                professorDTO,
                linkTo(methodOn(ProfessorController.class).findByIdProfessor(professorDTO.getId())).withSelfRel(),
                linkTo(methodOn(ProfessorController.class).findAllProfessors()).withRel("professor")
        );
    }
}
