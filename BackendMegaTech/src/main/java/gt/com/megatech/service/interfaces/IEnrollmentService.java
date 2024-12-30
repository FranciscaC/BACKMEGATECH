package gt.com.megatech.service.interfaces;

import gt.com.megatech.presentation.dto.EnrollmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IEnrollmentService {

    List<EnrollmentDTO> findAllEnrollments();

    Page<EnrollmentDTO> findAllEnrollmentsPaged(Pageable pageable);

    EnrollmentDTO findByIdEnrollment(Long id);

    EnrollmentDTO saveEnrollment(EnrollmentDTO enrollmentDTO);

    EnrollmentDTO updateEnrollment(Long id, EnrollmentDTO enrollmentDTO);

    void deleteEnrollment(Long id);
}
