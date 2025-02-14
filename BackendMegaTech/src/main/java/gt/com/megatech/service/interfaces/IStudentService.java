package gt.com.megatech.service.interfaces;

import gt.com.megatech.presentation.dto.StudentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IStudentService {

    List<StudentDTO> findAllStudyingStudents();

    List<StudentDTO> findAllSuspendedStudents();

    List<StudentDTO> findAllGraduatedStudents();

    List<StudentDTO> findAllEnrolledStudents();

    List<StudentDTO> findAllNotEnrolledStudents();

    Page<StudentDTO> findAllStudyingStudentsPaged(
            Pageable pageable
    );

    Page<StudentDTO> findAllSuspendedStudentsPaged(
            Pageable pageable
    );

    Page<StudentDTO> findAllGraduatedStudentsPaged(
            Pageable pageable
    );

    Page<StudentDTO> findAllEnrolledStudentsPaged(
            Pageable pageable
    );

    Page<StudentDTO> findAllNotEnrolledStudentsPaged(
            Pageable pageable
    );

    StudentDTO findByIdStudent(
            Long id
    );

    StudentDTO saveStudent(
            StudentDTO studentDTO
    );

    StudentDTO updateStudent(
            Long id,
            StudentDTO studentDTO
    );

    StudentDTO updateAcademicStatusToStudying(
            Long id
    );

    StudentDTO updateAcademicStatusToSuspended(
            Long id
    );

    StudentDTO updateAcademicStatusToGraduated(
            Long id
    );

    void deleteStudent(
            Long id
    );
}
