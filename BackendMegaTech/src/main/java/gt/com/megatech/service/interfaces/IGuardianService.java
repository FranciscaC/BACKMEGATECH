package gt.com.megatech.service.interfaces;

import gt.com.megatech.presentation.dto.GuardianDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IGuardianService {

    List<GuardianDTO> findAllGuardians();

    Page<GuardianDTO> findAllGuardiansPaged(Pageable pageable);

    List<GuardianDTO> findAllGuardiansWithStudents();

    Page<GuardianDTO> findAllGuardiansWithStudentsPaged(Pageable pageable);

    GuardianDTO findByIdGuardian(Long id);

    GuardianDTO findGuardianByIdWithStudents(Long id);

    GuardianDTO saveGuardian(GuardianDTO guardianDTO);

    GuardianDTO updateGuardian(Long id, GuardianDTO guardianDTO);

    void deleteGuardian(Long id);
}
