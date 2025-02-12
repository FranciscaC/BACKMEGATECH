package gt.com.megatech.service.implementation;

import gt.com.megatech.persistence.entity.*;
import gt.com.megatech.persistence.entity.enums.AcademicStatusEnum;
import gt.com.megatech.persistence.entity.enums.MonthEnum;
import gt.com.megatech.persistence.repository.*;
import gt.com.megatech.presentation.dto.GuardianDTO;
import gt.com.megatech.presentation.dto.StudentDTO;
import gt.com.megatech.service.exception.GraduatedStudentNotFoundException;
import gt.com.megatech.service.exception.GuardianNotFoundException;
import gt.com.megatech.service.exception.StudentNotFoundException;
import gt.com.megatech.service.interfaces.IStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImplementation implements IStudentService {

    private final IGuardianRepository iGuardianRepository;
    private final IStudentRepository iStudentRepository;
    private final IEnrollmentRepository iEnrollmentRepository;
    private final IPaymentRepository iPaymentRepository;
    private final ISuspendedStudentRepository iSuspendedStudentRepository;
    private final IGraduatedStudentRepository iGraduatedStudentRepository;

    private static final String UPDATED_ILLEGAL_STATE_EXCEPTION = "Failed to update academic status.";

    @Transactional(
            readOnly = true
    )
    @Override
    public List<StudentDTO> findAllStudyingStudents() {
        return this.iStudentRepository.findByAcademicStatusEnum(
                        AcademicStatusEnum.STUDYING
                ).stream()
                .map(this::convertToStudentDTO)
                .toList();
    }

    @Transactional(
            readOnly = true
    )
    @Override
    public List<StudentDTO> findAllSuspendedStudents() {
        return this.iStudentRepository.findByAcademicStatusEnum(
                        AcademicStatusEnum.SUSPENDED
                ).stream()
                .map(this::convertToStudentDTO)
                .toList();
    }

    @Transactional(
            readOnly = true
    )
    @Override
    public List<StudentDTO> findAllGraduatedStudents() {
        return this.iStudentRepository.findByAcademicStatusEnum(
                        AcademicStatusEnum.GRADUATED
                ).stream()
                .map(this::convertToStudentDTO)
                .toList();
    }

    @Transactional(
            readOnly = true
    )
    @Override
    public Page<StudentDTO> findAllStudyingStudentsPaged(
            Pageable pageable
    ) {
        return this.iStudentRepository.findByAcademicStatusEnum(
                AcademicStatusEnum.STUDYING,
                pageable
        ).map(this::convertToStudentDTO);
    }

    @Transactional(
            readOnly = true
    )
    @Override
    public Page<StudentDTO> findAllSuspendedStudentsPaged(
            Pageable pageable
    ) {
        return this.iStudentRepository.findByAcademicStatusEnum(
                AcademicStatusEnum.SUSPENDED,
                pageable
        ).map(this::convertToStudentDTO);
    }

    @Transactional(
            readOnly = true
    )
    @Override
    public Page<StudentDTO> findAllGraduatedStudentsPaged(
            Pageable pageable
    ) {
        return this.iStudentRepository.findByAcademicStatusEnum(
                AcademicStatusEnum.GRADUATED,
                pageable
        ).map(this::convertToStudentDTO);
    }

    @Transactional(
            readOnly = true
    )
    @Override
    public StudentDTO findByIdStudent(
            Long id
    ) {
        StudentEntity studentEntityExists = this.iStudentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        return this.convertToStudentDTO(studentEntityExists);
    }

    @Transactional
    @Override
    public StudentDTO saveStudent(
            StudentDTO studentDTO
    ) {
        if (iStudentRepository.existsByName(studentDTO.getName())) {
            throw new IllegalArgumentException("A student with this name already exists.");
        }
        if (iStudentRepository.existsByPhone(studentDTO.getPhone())) {
            throw new IllegalArgumentException("A student with this phone number already exists.");
        }
        if (iStudentRepository.existsByEmail(studentDTO.getEmail())) {
            throw new IllegalArgumentException("A student with this email number already exists.");
        }
        GuardianEntity guardianEntityExists = this.iGuardianRepository.findById(studentDTO.getGuardianDTO().getId())
                .orElseThrow(() -> new GuardianNotFoundException(studentDTO.getGuardianDTO().getId()));
        StudentEntity studentEntity = this.convertToStudentEntity(studentDTO);
        studentEntity.setName(studentDTO.getName());
        studentEntity.setCui(studentDTO.getCui());
        studentEntity.setPersonalCode(studentDTO.getPersonalCode());
        studentEntity.setBirthDate(studentDTO.getBirthDate());
        studentEntity.setPhone(studentDTO.getPhone());
        studentEntity.setEmail(studentDTO.getEmail());
        studentEntity.setAddress(studentDTO.getAddress());
        studentEntity.setEducationLevel(studentDTO.getEducationLevel());
        studentEntity.setAcademicStatusEnum(AcademicStatusEnum.STUDYING);
        studentEntity.setGuardianEntity(guardianEntityExists);
        StudentEntity studentEntitySaved = this.iStudentRepository.save(studentEntity);
        return this.convertToStudentDTO(studentEntitySaved);
    }

    @Transactional
    @Override
    public StudentDTO updateStudent(
            Long id,
            StudentDTO studentDTO
    ) {
        GuardianEntity guardianEntityExists = this.iGuardianRepository.findById(studentDTO.getGuardianDTO().getId())
                .orElseThrow(() -> new GuardianNotFoundException(studentDTO.getGuardianDTO().getId()));
        this.iStudentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        int rowsAffected = this.iStudentRepository.updateStudentById(
                id,
                studentDTO.getName(),
                studentDTO.getCui(),
                studentDTO.getPersonalCode(),
                studentDTO.getBirthDate(),
                studentDTO.getPhone(),
                studentDTO.getEmail(),
                studentDTO.getAddress(),
                studentDTO.getEducationLevel(),
                studentDTO.getAcademicStatusEnum(),
                guardianEntityExists
        );
        if (rowsAffected == 0) {
            throw new IllegalStateException("No student could be updated with ID: " + id);
        }
        StudentEntity studentUpdated = this.iStudentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        return convertToStudentDTO(studentUpdated);
    }

    @Transactional
    @Override
    public StudentDTO updateAcademicStatusToStudying(
            Long id
    ) {
        if (!this.iStudentRepository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        int rowsUpdated = this.iStudentRepository.updateAcademicStatusById(
                id,
                AcademicStatusEnum.STUDYING
        );
        if (rowsUpdated == 0) {
            throw new IllegalStateException(UPDATED_ILLEGAL_STATE_EXCEPTION);
        }
        StudentEntity updatedStudent = this.iStudentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        return convertToStudentDTO(updatedStudent);
    }

    @Transactional
    @Override
    public StudentDTO updateAcademicStatusToSuspended(
            Long id
    ) {
        if (!this.iStudentRepository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        int rowsUpdated = this.iStudentRepository.updateAcademicStatusById(
                id,
                AcademicStatusEnum.SUSPENDED
        );
        if (rowsUpdated == 0) {
            throw new IllegalStateException(UPDATED_ILLEGAL_STATE_EXCEPTION);
        }
        StudentEntity updatedStudent = this.iStudentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        return convertToStudentDTO(updatedStudent);
    }

    @Transactional
    @Override
    public StudentDTO updateAcademicStatusToGraduated(
            Long id
    ) {
        if (!this.iStudentRepository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        int rowsUpdated = this.iStudentRepository.updateAcademicStatusById(
                id,
                AcademicStatusEnum.GRADUATED
        );
        if (rowsUpdated == 0) {
            throw new IllegalStateException(UPDATED_ILLEGAL_STATE_EXCEPTION);
        }
        StudentEntity updatedStudent = this.iStudentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        LocalDate localDate = LocalDate.now();
        MonthEnum monthEnum = MonthEnum.fromJavaMonth(localDate.getMonth());
        GraduatedStudentEntity graduatedStudentEntity = GraduatedStudentEntity.builder()
                .graduationMonth(monthEnum)
                .graduationYear(localDate.getYear())
                .graduationDate(localDate)
                .studentEntity(updatedStudent)
                .build();
        this.iGraduatedStudentRepository.save(graduatedStudentEntity);
        return convertToStudentDTO(updatedStudent);
    }

    @Transactional
    @Override
    public void deleteStudent(Long id) {
        StudentEntity student = iStudentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        GraduatedStudentEntity graduatedRecord = iGraduatedStudentRepository
                .findByStudentEntity_Id(student.getId())
                .orElseThrow(() -> new GraduatedStudentNotFoundException(student.getId()));
        if (student.getEnrollmentEntity() != null) {
            EnrollmentEntity enrollment = student.getEnrollmentEntity();
            student.setEnrollmentEntity(null);
            enrollment.setStudentEntity(null);
            iEnrollmentRepository.delete(enrollment);
        }
        List<PaymentEntity> payments = iPaymentRepository.findByStudentEntity_Id(student.getId());
        if (payments != null && !payments.isEmpty()) {
            iPaymentRepository.deleteAll(payments);
        }
        List<SuspendedStudentEntity> suspensions = iSuspendedStudentRepository.findByStudentEntity_Id(student.getId());
        if (suspensions != null && !suspensions.isEmpty()) {
            iSuspendedStudentRepository.deleteAll(suspensions);
        }
        iGraduatedStudentRepository.delete(graduatedRecord);
        iStudentRepository.delete(student);
    }

    private StudentDTO convertToStudentDTO(
            StudentEntity studentEntity
    ) {
        return StudentDTO.builder()
                .id(studentEntity.getId())
                .name(studentEntity.getName())
                .cui(studentEntity.getCui())
                .personalCode(studentEntity.getPersonalCode())
                .birthDate(studentEntity.getBirthDate())
                .phone(studentEntity.getPhone())
                .email(studentEntity.getEmail())
                .address(studentEntity.getAddress())
                .educationLevel(studentEntity.getEducationLevel())
                .academicStatusEnum(studentEntity.getAcademicStatusEnum())
                .guardianDTO(convertToGuardianDTO(studentEntity.getGuardianEntity()))
                .build();
    }

    private StudentEntity convertToStudentEntity(
            StudentDTO studentDTO
    ) {
        return StudentEntity.builder()
                .name(studentDTO.getName())
                .cui(studentDTO.getCui())
                .personalCode(studentDTO.getPersonalCode())
                .birthDate(studentDTO.getBirthDate())
                .phone(studentDTO.getPhone())
                .email(studentDTO.getEmail())
                .address(studentDTO.getAddress())
                .educationLevel(studentDTO.getEducationLevel())
                .academicStatusEnum(studentDTO.getAcademicStatusEnum())
                .build();
    }

    private GuardianDTO convertToGuardianDTO(
            GuardianEntity guardianEntity
    ) {
        return GuardianDTO.builder()
                .id(guardianEntity.getId())
                .name(guardianEntity.getName())
                .phone(guardianEntity.getPhone())
                .address(guardianEntity.getAddress())
                .build();
    }
}
