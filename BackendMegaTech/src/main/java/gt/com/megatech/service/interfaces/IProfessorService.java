package gt.com.megatech.service.interfaces;

import gt.com.megatech.presentation.dto.ProfessorDTO;

import java.util.List;

public interface IProfessorService {

    List<ProfessorDTO> findAllProfessors();

    ProfessorDTO findByIdProfessor(
            Long id
    );

    ProfessorDTO saveProfessor(
            ProfessorDTO professorDTO
    );

    ProfessorDTO updateProfessor(
            Long id,
            ProfessorDTO professorDTO
    );

    void deleteProfessor(
            Long id
    );
}
