package gt.com.megatech.service.interfaces;

import gt.com.megatech.presentation.dto.EnrollmentDTO;

import java.util.List;

public interface IEnrollmentService {

    List<EnrollmentDTO> findAllEnrollments();

    EnrollmentDTO findByIdEnrollment(
            Long id
    );

    EnrollmentDTO saveEnrollment(
            EnrollmentDTO enrollmentDTO
    );

    EnrollmentDTO updateEnrollment(
            Long id,
            EnrollmentDTO enrollmentDTO
    );

    void deleteEnrollment(
            Long id
    );
}
