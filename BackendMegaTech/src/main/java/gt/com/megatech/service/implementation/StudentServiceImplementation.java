package gt.com.megatech.service.implementation;

import gt.com.megatech.persistence.entity.GuardianEntity;
import gt.com.megatech.persistence.entity.StudentEntity;
import gt.com.megatech.persistence.repository.IGuardianRepository;
import gt.com.megatech.persistence.repository.IStudentRepository;
import gt.com.megatech.presentation.dto.GuardianDTO;
import gt.com.megatech.presentation.dto.StudentDTO;
import gt.com.megatech.service.exception.GuardianNotFoundException;
import gt.com.megatech.service.exception.StudentNotFoundException;
import gt.com.megatech.service.interfaces.IStudentService;
import lombok.RequiredArgsConstructor;
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
    public List<StudentDTO> findAllStudent() {
        return this.iStudentRepository.findAll()
                .stream()
                .map(this::convertToStudentDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public StudentDTO findByIdStudent(Long id) {
        StudentEntity studentEntity = this.iStudentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        return this.convertToStudentDTO(studentEntity);
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
        studentEntityExists.setEducationLevel(studentDTO.getEducationLevel());
        StudentEntity studentUpdated = this.iStudentRepository.save(studentEntityExists);
        return convertToStudentDTO(studentUpdated);
    }

    @Transactional
    @Override
    public void deleteStudent(Long id) {
        StudentEntity studentEntity = this.iStudentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        this.iStudentRepository.delete(studentEntity);
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
                .educationLevel(studentDTO.getEducationLevel())
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
