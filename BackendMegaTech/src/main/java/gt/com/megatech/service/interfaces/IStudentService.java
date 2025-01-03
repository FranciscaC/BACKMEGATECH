package gt.com.megatech.service.interfaces;

import gt.com.megatech.persistence.entity.enums.AcademicStatusEnum;
import gt.com.megatech.presentation.dto.StudentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IStudentService {

    List<StudentDTO> findAllStudyingStudents();

    List<StudentDTO> findAllGraduatedStudents();

    Page<StudentDTO> findAllStudyingStudentsPaged(Pageable pageable);

    Page<StudentDTO> findAllGraduatedStudentsPaged(Pageable pageable);

    StudentDTO findByIdStudent(Long id);

    StudentDTO saveStudent(StudentDTO studentDTO);

    StudentDTO updateStudent(Long id, StudentDTO studentDTO);

    StudentDTO updateAcademicStatusToGraduated(Long id);

    void deleteStudent(Long id);
}
