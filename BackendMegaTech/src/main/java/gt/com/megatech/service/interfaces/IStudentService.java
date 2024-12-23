package gt.com.megatech.service.interfaces;

import gt.com.megatech.presentation.dto.StudentDTO;

import java.util.List;

public interface IStudentService {

    List<StudentDTO> findAllStudent();

    StudentDTO findByIdStudent(Long id);

    StudentDTO saveStudent(StudentDTO studentDTO);

    StudentDTO updateStudent(Long id, StudentDTO studentDTO);

    void deleteStudent(Long id);
}
