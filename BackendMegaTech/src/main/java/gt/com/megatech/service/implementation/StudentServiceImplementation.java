package gt.com.megatech.service.implementation;

import gt.com.megatech.persistence.entity.GuardianEntity;
import gt.com.megatech.persistence.entity.StudentEntity;
import gt.com.megatech.persistence.entity.enums.AcademicStatusEnum;
import gt.com.megatech.persistence.repository.IGuardianRepository;
import gt.com.megatech.persistence.repository.IStudentRepository;
import gt.com.megatech.presentation.dto.GuardianDTO;
import gt.com.megatech.presentation.dto.StudentDTO;
import gt.com.megatech.service.exception.GuardianNotFoundException;
import gt.com.megatech.service.exception.StudentNotFoundException;
import gt.com.megatech.service.interfaces.IStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImplementation implements IStudentService {

    private final IGuardianRepository iGuardianRepository;
    private final IStudentRepository iStudentRepository;

    @Transactional(readOnly = true)
    @Override
    public List<StudentDTO> findAllStudyingStudents() {
        return this.iStudentRepository.findByAcademicStatusEnum(AcademicStatusEnum.STUDYING)
                .stream()
                .map(this::convertToStudentDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<StudentDTO> findAllGraduatedStudents() {
        return this.iStudentRepository.findByAcademicStatusEnum(AcademicStatusEnum.GRADUATED)
                .stream()
                .map(this::convertToStudentDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<StudentDTO> findAllStudyingStudentsPaged(Pageable pageable) {
        return this.iStudentRepository.findByAcademicStatusEnum(AcademicStatusEnum.STUDYING, pageable)
                .map(this::convertToStudentDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<StudentDTO> findAllGraduatedStudentsPaged(Pageable pageable) {
        return this.iStudentRepository.findByAcademicStatusEnum(AcademicStatusEnum.GRADUATED, pageable)
                .map(this::convertToStudentDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public StudentDTO findByIdStudent(Long id) {
        StudentEntity studentEntityExists = this.iStudentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        return this.convertToStudentDTO(studentEntityExists);
    }

    @Transactional
    @Override
    public StudentDTO saveStudent(StudentDTO studentDTO) {
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
        studentEntity.setBirthDate(studentDTO.getBirthDate());
        studentEntity.setPhone(studentDTO.getPhone());
        studentEntity.setEmail(studentDTO.getEmail());
        studentEntity.setAddress(studentDTO.getAddress());
        studentEntity.setEducationLevelEnum(studentDTO.getEducationLevelEnum());
        studentEntity.setAcademicStatusEnum(AcademicStatusEnum.STUDYING);
        studentEntity.setGuardianEntity(guardianEntityExists);
        StudentEntity studentEntitySaved = this.iStudentRepository.save(studentEntity);
        return this.convertToStudentDTO(studentEntitySaved);
    }

    @Transactional
    @Override
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        StudentEntity studentEntityExists = this.iStudentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        studentEntityExists.setName(studentDTO.getName());
        studentEntityExists.setBirthDate(studentDTO.getBirthDate());
        studentEntityExists.setPhone(studentDTO.getPhone());
        studentEntityExists.setEmail(studentDTO.getEmail());
        studentEntityExists.setAddress(studentDTO.getAddress());
        studentEntityExists.setEducationLevelEnum(studentDTO.getEducationLevelEnum());
        studentEntityExists.setAcademicStatusEnum(studentDTO.getAcademicStatusEnum());
        StudentEntity studentUpdated = this.iStudentRepository.save(studentEntityExists);
        return convertToStudentDTO(studentUpdated);
    }

    @Transactional
    @Override
    public StudentDTO updateAcademicStatusToGraduated(Long id) {
        if (!this.iStudentRepository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        int rowsUpdated = this.iStudentRepository.updateAcademicStatusById(id, AcademicStatusEnum.GRADUATED);
        if (rowsUpdated == 0) {
            throw new IllegalStateException("No se pudo actualizar el estado académico.");
        }
        StudentEntity updatedStudent = this.iStudentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        return convertToStudentDTO(updatedStudent);
    }

    @Transactional
    @Override
    public void deleteStudent(Long id) {
        StudentEntity studentEntityExists = this.iStudentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        this.iStudentRepository.delete(studentEntityExists);
    }

    private StudentDTO convertToStudentDTO(StudentEntity studentEntity) {
        return StudentDTO.builder()
                .id(studentEntity.getId())
                .name(studentEntity.getName())
                .birthDate(studentEntity.getBirthDate())
                .phone(studentEntity.getPhone())
                .email(studentEntity.getEmail())
                .address(studentEntity.getAddress())
                .educationLevelEnum(studentEntity.getEducationLevelEnum())
                .academicStatusEnum(studentEntity.getAcademicStatusEnum())
                .guardianDTO(convertToGuardianDTO(studentEntity.getGuardianEntity()))
                .build();
    }

    private StudentEntity convertToStudentEntity(StudentDTO studentDTO) {
        return StudentEntity.builder()
                .name(studentDTO.getName())
                .birthDate(studentDTO.getBirthDate())
                .phone(studentDTO.getPhone())
                .email(studentDTO.getEmail())
                .address(studentDTO.getAddress())
                .educationLevelEnum(studentDTO.getEducationLevelEnum())
                .academicStatusEnum(studentDTO.getAcademicStatusEnum())
                .build();
    }

    private GuardianDTO convertToGuardianDTO(GuardianEntity guardianEntity) {
        return GuardianDTO.builder()
                .id(guardianEntity.getId())
                .name(guardianEntity.getName())
                .phone(guardianEntity.getPhone())
                .address(guardianEntity.getAddress())
                .build();
    }
}
