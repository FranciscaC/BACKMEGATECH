package gt.com.megatech.service.interfaces;

import gt.com.megatech.presentation.dto.StudentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IStudentService {

    List<StudentDTO> findAllStudent();

    Page<StudentDTO> findAllStudentPaged(Pageable pageable);

    StudentDTO findByIdStudent(Long id);

    StudentDTO saveStudent(StudentDTO studentDTO);

    StudentDTO updateStudent(Long id, StudentDTO studentDTO);

    void deleteStudent(Long id);
}
