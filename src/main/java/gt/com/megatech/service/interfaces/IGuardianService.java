package gt.com.megatech.service.interfaces;

import gt.com.megatech.presentation.dto.GuardianDTO;

import java.util.List;

public interface IGuardianService {

    List<GuardianDTO> findAllGuardians();

    List<GuardianDTO> findAllGuardiansWithStudents();

    GuardianDTO findByIdGuardian(
            Long id
    );

    GuardianDTO findGuardianByIdWithStudents(
            Long id
    );

    GuardianDTO saveGuardian(
            GuardianDTO guardianDTO
    );

    GuardianDTO updateGuardian(
            Long id,
            GuardianDTO guardianDTO
    );

    void deleteGuardian(
            Long id
    );
}
