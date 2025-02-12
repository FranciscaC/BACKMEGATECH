package gt.com.megatech.service.implementation;

import gt.com.megatech.persistence.entity.GraduatedStudentEntity;
import gt.com.megatech.persistence.entity.StudentEntity;
import gt.com.megatech.persistence.repository.IGraduatedStudentRepository;
import gt.com.megatech.presentation.dto.GraduatedStudentDTO;
import gt.com.megatech.presentation.dto.StudentDTO;
import gt.com.megatech.service.exception.GraduatedStudentNotFoundException;
import gt.com.megatech.service.interfaces.IGraduatedStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GraduatedStudentServiceImplementation implements IGraduatedStudentService {

    private final IGraduatedStudentRepository iGraduatedStudentRepository;

    @Transactional(
            readOnly = true
    )
    @Override
    public List<GraduatedStudentDTO> findAllGraduatedStudents() {
        return this.iGraduatedStudentRepository.findAll()
                .stream()
                .map(this::convertToGraduatedStudentDTO)
                .toList();
    }

    @Transactional(
            readOnly = true
    )
    @Override
    public Page<GraduatedStudentDTO> findAllGraduatedStudents(
            Pageable pageable
    ) {
        return this.iGraduatedStudentRepository.findAll(pageable)
                .map(this::convertToGraduatedStudentDTO);
    }

    @Transactional(
            readOnly = true
    )
    @Override
    public GraduatedStudentDTO findByIdGraduatedStudent(Long id) {
        GraduatedStudentEntity graduatedStudentEntity = this.iGraduatedStudentRepository.findById(id)
                .orElseThrow(() -> new GraduatedStudentNotFoundException(id));
        return this.convertToGraduatedStudentDTO(graduatedStudentEntity);
    }

    @Override
    public void deleteGraduatedStudent(Long id) {
        GraduatedStudentEntity graduatedStudentEntity = this.iGraduatedStudentRepository.findById(
                id
        ).orElseThrow(() -> new GraduatedStudentNotFoundException(id));
        this.iGraduatedStudentRepository.delete(graduatedStudentEntity);
    }

    private GraduatedStudentDTO convertToGraduatedStudentDTO(
            GraduatedStudentEntity graduatedStudentEntity
    ) {
        return GraduatedStudentDTO.builder()
                .id(graduatedStudentEntity.getId())
                .graduationMonth(graduatedStudentEntity.getGraduationMonth())
                .graduationYear(graduatedStudentEntity.getGraduationYear())
                .graduationDate(graduatedStudentEntity.getGraduationDate())
                .studentDTO(convertToStudentDTO(graduatedStudentEntity.getStudentEntity()))
                .build();
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
                .build();
    }
}
