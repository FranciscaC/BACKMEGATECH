package gt.com.megatech.service.implementation;

import gt.com.megatech.persistence.entity.StudentEntity;
import gt.com.megatech.persistence.entity.SuspendedStudentEntity;
import gt.com.megatech.persistence.repository.IStudentRepository;
import gt.com.megatech.persistence.repository.ISuspendedStudentRepository;
import gt.com.megatech.presentation.dto.StudentDTO;
import gt.com.megatech.presentation.dto.SuspendedStudentDTO;
import gt.com.megatech.service.exception.SuspendedStudentNotFoundException;
import gt.com.megatech.service.interfaces.IStudentService;
import gt.com.megatech.service.interfaces.ISuspendedStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SuspendedStudentServiceImplementation implements ISuspendedStudentService {

    private final IStudentService iStudentService;
    private final IStudentRepository iStudentRepository;
    private final ISuspendedStudentRepository iSuspendedStudentRepository;

    @Transactional(
            readOnly = true
    )
    @Override
    public List<SuspendedStudentDTO> findAllSuspendedStudents() {
        return this.iSuspendedStudentRepository.findAll()
                .stream()
                .map(this::convertToSuspendedStudentDTO)
                .toList();
    }

    @Transactional(
            readOnly = true
    )
    @Override
    public Page<SuspendedStudentDTO> findAllSuspendedStudents(
            Pageable pageable
    ) {
        return this.iSuspendedStudentRepository.findAll(pageable)
                .map(this::convertToSuspendedStudentDTO);
    }

    @Transactional(
            readOnly = true
    )
    @Override
    public SuspendedStudentDTO findByIdSuspendedStudent(
            Long id
    ) {
        SuspendedStudentEntity suspendedStudentEntity = this.iSuspendedStudentRepository.findById(id)
                .orElseThrow(() -> new SuspendedStudentNotFoundException(id));
        return this.convertToSuspendedStudentDTO(suspendedStudentEntity);
    }

    @Transactional
    @Override
    public SuspendedStudentDTO saveSuspendedStudent(SuspendedStudentDTO suspendedStudentDTO) {
        StudentDTO updatedStudent = iStudentService.updateAcademicStatusToSuspended(
                suspendedStudentDTO.getStudentDTO().getId()
        );
        SuspendedStudentEntity suspendedStudentEntity = this.converToSuspendedStudentEntity(
                suspendedStudentDTO
        );
        suspendedStudentEntity.setSuspensionDate(suspendedStudentDTO.getSuspensionDate());
        suspendedStudentEntity.setStudentEntity(convertToStudentEntity(updatedStudent));
        SuspendedStudentEntity suspendedStudentEntitySaved = iSuspendedStudentRepository.save(
                suspendedStudentEntity);
        return this.convertToSuspendedStudentDTO(suspendedStudentEntitySaved
        );
    }

    @Transactional
    @Override
    public SuspendedStudentDTO updateSuspendedStudent(
            Long id,
            SuspendedStudentDTO suspendedStudentDTO
    ) {
        SuspendedStudentEntity suspendedStudentEntityExists = this.iSuspendedStudentRepository.findById(
                id
        ).orElseThrow(() -> new SuspendedStudentNotFoundException(
                id
        ));
        suspendedStudentEntityExists.setSuspensionDate(suspendedStudentDTO.getSuspensionDate());
        suspendedStudentEntityExists.setReentryDate(suspendedStudentDTO.getReentryDate());
        if (suspendedStudentDTO.getReentryDate() != null) {
            Long studentId = suspendedStudentEntityExists.getStudentEntity().getId();
            this.iStudentService.updateAcademicStatusToStudying(studentId);
        }
        SuspendedStudentEntity suspendedStudentEntityUpdated = this.iSuspendedStudentRepository.save(
                suspendedStudentEntityExists
        );
        return this.convertToSuspendedStudentDTO(suspendedStudentEntityUpdated);
    }

    @Transactional
    @Override
    public void deleteSuspendedStudent(Long id) {
        SuspendedStudentEntity suspendedStudentEntityExists = this.iSuspendedStudentRepository.findById(
                id
        ).orElseThrow(() -> new SuspendedStudentNotFoundException(id));
        this.iSuspendedStudentRepository.delete(suspendedStudentEntityExists);
    }

    private SuspendedStudentDTO convertToSuspendedStudentDTO(
            SuspendedStudentEntity suspendedStudentEntity
    ) {
        return SuspendedStudentDTO.builder()
                .id(suspendedStudentEntity.getId())
                .suspensionDate(suspendedStudentEntity.getSuspensionDate())
                .reentryDate(suspendedStudentEntity.getReentryDate())
                .studentDTO(convertToStudentDTO(suspendedStudentEntity.getStudentEntity()))
                .build();
    }

    private SuspendedStudentEntity converToSuspendedStudentEntity(
            SuspendedStudentDTO suspendedStudentDTO
    ) {
        return SuspendedStudentEntity.builder()
                .suspensionDate(suspendedStudentDTO.getSuspensionDate())
                .reentryDate(suspendedStudentDTO.getReentryDate())
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

    private StudentEntity convertToStudentEntity(
            StudentDTO studentDTO
    ) {
        return StudentEntity.builder()
                .id(studentDTO.getId())
                .name(studentDTO.getName())
                .cui(studentDTO.getCui())
                .personalCode(studentDTO.getPersonalCode())
                .birthDate(studentDTO.getBirthDate())
                .phone(studentDTO.getPhone())
                .email(studentDTO.getEmail())
                .address(studentDTO.getAddress())
                .educationLevel(studentDTO.getEducationLevel())
                .build();
    }
}
