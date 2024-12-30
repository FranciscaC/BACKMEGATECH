package gt.com.megatech.service.implementation;

import gt.com.megatech.persistence.entity.EnrollmentEntity;
import gt.com.megatech.persistence.entity.StudentEntity;
import gt.com.megatech.persistence.repository.IEnrollmentRepository;
import gt.com.megatech.persistence.repository.IStudentRepository;
import gt.com.megatech.presentation.dto.EnrollmentDTO;
import gt.com.megatech.presentation.dto.StudentDTO;
import gt.com.megatech.service.exception.EnrollmentNotFoundException;
import gt.com.megatech.service.exception.StudentNotFoundException;
import gt.com.megatech.service.interfaces.IEnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImplementation implements IEnrollmentService {

    private final IEnrollmentRepository iEnrollmentRepository;
    private final IStudentRepository iStudentRepository;

    @Transactional(readOnly = true)
    @Override
    public List<EnrollmentDTO> findAllEnrollments() {
        return this.iEnrollmentRepository.findAll()
                .stream()
                .map(this::convertToEnrollmentDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<EnrollmentDTO> findAllEnrollmentsPaged(Pageable pageable) {
        return this.iEnrollmentRepository.findAll(pageable)
                .map(this::convertToEnrollmentDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public EnrollmentDTO findByIdEnrollment(Long id) {
        EnrollmentEntity enrollmentEntity = this.iEnrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException(id));
        return this.convertToEnrollmentDTO(enrollmentEntity);
    }

    @Transactional
    @Override
    public EnrollmentDTO saveEnrollment(EnrollmentDTO enrollmentDTO) {
        StudentEntity studentEntityExists = this.iStudentRepository.findById(enrollmentDTO.getStudentDTO().getId())
                .orElseThrow(() -> new StudentNotFoundException(enrollmentDTO.getStudentDTO().getId()));
        EnrollmentEntity enrollmentEntity = this.convertToEnrollmentEntity(enrollmentDTO);
        enrollmentEntity.setStudentEntity(studentEntityExists);
        EnrollmentEntity enrollmentEntitySaved = this.iEnrollmentRepository.save(enrollmentEntity);
        return this.convertToEnrollmentDTO(enrollmentEntitySaved);
    }

    @Transactional
    @Override
    public EnrollmentDTO updateEnrollment(Long id, EnrollmentDTO enrollmentDTO) {
        EnrollmentEntity enrollmentEntityExists = this.iEnrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException(id));
        enrollmentEntityExists.setEnrollmentDate(enrollmentDTO.getEnrollmentDate());
        enrollmentEntityExists.setPaymentAmount(enrollmentDTO.getPaymentAmount());
        EnrollmentEntity enrollmentEntityUpdated = this.iEnrollmentRepository.save(enrollmentEntityExists);
        return this.convertToEnrollmentDTO(enrollmentEntityUpdated);
    }

    @Transactional
    @Override
    public void deleteEnrollment(Long id) {
        EnrollmentEntity enrollmentEntity = this.iEnrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException(id));
        this.iEnrollmentRepository.delete(enrollmentEntity);
    }

    private EnrollmentDTO convertToEnrollmentDTO(EnrollmentEntity enrollmentEntity) {
        return EnrollmentDTO.builder()
                .id(enrollmentEntity.getId())
                .enrollmentDate(enrollmentEntity.getEnrollmentDate())
                .paymentAmount(enrollmentEntity.getPaymentAmount())
                .studentDTO(convertToStudentDTO(enrollmentEntity.getStudentEntity()))
                .build();
    }

    private EnrollmentEntity convertToEnrollmentEntity(EnrollmentDTO enrollmentDTO) {
        return EnrollmentEntity.builder()
                .enrollmentDate(enrollmentDTO.getEnrollmentDate())
                .paymentAmount(enrollmentDTO.getPaymentAmount())
                .build();
    }

    private StudentDTO convertToStudentDTO(StudentEntity studentEntity) {
        return StudentDTO.builder()
                .id(studentEntity.getId())
                .name(studentEntity.getName())
                .birthDate(studentEntity.getBirthDate())
                .phone(studentEntity.getPhone())
                .email(studentEntity.getEmail())
                .address(studentEntity.getAddress())
                .educationLevel(studentEntity.getEducationLevel())
                .build();
    }
}
