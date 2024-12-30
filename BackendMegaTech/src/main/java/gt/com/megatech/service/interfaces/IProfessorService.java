package gt.com.megatech.service.interfaces;

import gt.com.megatech.presentation.dto.ProfessorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProfessorService {

    List<ProfessorDTO> findAllProfessors();

    Page<ProfessorDTO> findAllProfessorsPaged(Pageable pageable);

    ProfessorDTO findByIdProfessor(Long id);

    ProfessorDTO saveProfessor(ProfessorDTO professorDTO);

    ProfessorDTO updateProfessor(Long id, ProfessorDTO professorDTO);

    void deleteProfessor(Long id);
}
