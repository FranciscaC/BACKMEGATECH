package gt.com.megatech.service.interfaces;

import gt.com.megatech.presentation.dto.GraduatedStudentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IGraduatedStudentService {

    List<GraduatedStudentDTO> findAllGraduatedStudents();

    Page<GraduatedStudentDTO> findAllGraduatedStudents(
            Pageable pageable
    );

    GraduatedStudentDTO findByIdGraduatedStudent(
            Long id
    );

    void deleteGraduatedStudent(
            Long id
    );
}
