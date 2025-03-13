package gt.com.megatech.service.interfaces;

import gt.com.megatech.presentation.dto.SuspendedStudentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ISuspendedStudentService {

    List<SuspendedStudentDTO> findAllSuspendedStudents();

    Page<SuspendedStudentDTO> findAllSuspendedStudents(
            Pageable pageable
    );

    SuspendedStudentDTO findByIdSuspendedStudent(
            Long id
    );

    SuspendedStudentDTO saveSuspendedStudent(
            SuspendedStudentDTO suspendedStudentDTO
    );

    SuspendedStudentDTO updateSuspendedStudent(
            Long id,
            SuspendedStudentDTO suspendedStudentDTO
    );

    void deleteSuspendedStudent(
            Long id
    );
}
