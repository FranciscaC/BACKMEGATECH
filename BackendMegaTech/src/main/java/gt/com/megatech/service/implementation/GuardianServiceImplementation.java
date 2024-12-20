package gt.com.megatech.service.implementation;

import gt.com.megatech.persistence.entity.GuardianEntity;
import gt.com.megatech.persistence.entity.StudentEntity;
import gt.com.megatech.persistence.repository.IGuardianRepository;
import gt.com.megatech.presentation.dto.GuardianDTO;
import gt.com.megatech.presentation.dto.StudentDTO;
import gt.com.megatech.service.exception.GuardianNotFoundException;
import gt.com.megatech.service.interfaces.IGuardianService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuardianServiceImplementation implements IGuardianService {

    private final IGuardianRepository iGuardianRepository;

    @Transactional(readOnly = true)
    @Override
    public List<GuardianDTO> findAllGuardians() {
        return this.iGuardianRepository.findAll()
                .stream()
                .map(this::convertToGuardianDTOWithoutStudents)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<GuardianDTO> findAllGuardiansWithStudents() {
        return this.iGuardianRepository.findAllGuardiansWithStudents()
                .stream()
                .map(this::convertToGuardianDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public GuardianDTO findByIdGuardian(Long id) {
        GuardianEntity guardianEntityExists = this.iGuardianRepository.findById(id)
                .orElseThrow(() -> new GuardianNotFoundException(id));
        return this.convertToGuardianDTOWithoutStudents(guardianEntityExists);
    }

    @Transactional(readOnly = true)
    @Override
    public GuardianDTO findGuardianByIdWithStudents(Long id) {
        GuardianEntity guardianEntityExists = this.iGuardianRepository.findGuardianByIdWithStudents(id)
                .orElseThrow(() -> new GuardianNotFoundException(id));
        return this.convertToGuardianDTO(guardianEntityExists);
    }

    @Transactional
    @Override
    public GuardianDTO saveGuardian(GuardianDTO guardianDTO) {
        if (iGuardianRepository.existsByName(guardianDTO.getName())) {
            throw new IllegalArgumentException("A guardian with this name already exists.");
        }
        if (iGuardianRepository.existsByPhone(guardianDTO.getPhone())) {
            throw new IllegalArgumentException("A guardian with this phone number already exists.");
        }
        GuardianEntity guardianEntity = this.convertToGuardianEntity(guardianDTO);
        GuardianEntity guardianEntitySaved = this.iGuardianRepository.save(guardianEntity);
        return this.convertToGuardianDTO(guardianEntitySaved);
    }

    @Transactional
    @Override
    public GuardianDTO updateGuardian(Long id, GuardianDTO guardianDTO) {
        GuardianEntity guardianEntityExists = this.iGuardianRepository.findById(id)
                .orElseThrow(() -> new GuardianNotFoundException(id));
        guardianEntityExists.setName(guardianDTO.getName());
        guardianEntityExists.setPhone(guardianDTO.getPhone());
        guardianEntityExists.setAddress(guardianDTO.getAddress());
        GuardianEntity guardianEntityUpdated = this.iGuardianRepository.save(guardianEntityExists);
        return this.convertToGuardianDTO(guardianEntityUpdated);
    }

    @Transactional
    @Override
    public void deleteGuardian(Long id) {
        GuardianEntity guardianEntity = this.iGuardianRepository.findById(id)
                .orElseThrow(() -> new GuardianNotFoundException(id));
        this.iGuardianRepository.delete(guardianEntity);
    }

    private GuardianDTO convertToGuardianDTO(GuardianEntity guardianEntity) {
        return GuardianDTO.builder()
                .id(guardianEntity.getId())
                .name(guardianEntity.getName())
                .phone(guardianEntity.getPhone())
                .address(guardianEntity.getAddress())
                .studentDTOS(guardianEntity.getStudentEntities().stream()
                        .map(studentEntity -> StudentDTO.builder()
                                .id(studentEntity.getId())
                                .name(studentEntity.getName())
                                .birthDate(studentEntity.getBirthDate())
                                .phone(studentEntity.getPhone())
                                .email(studentEntity.getEmail())
                                .address(studentEntity.getAddress())
                                .educationLevel(studentEntity.getEducationLevel())
                                .build())
                        .collect(Collectors.toSet()))
                .build();
    }

    private GuardianEntity convertToGuardianEntity(GuardianDTO guardianDTO) {
        return GuardianEntity.builder()
                .name(guardianDTO.getName())
                .phone(guardianDTO.getPhone())
                .address(guardianDTO.getAddress())
                .studentEntities(guardianDTO.getStudentDTOS().stream()
                        .map(studentDTO -> StudentEntity.builder()
                                .id(studentDTO.getId())
                                .name(studentDTO.getName())
                                .birthDate(studentDTO.getBirthDate())
                                .phone(studentDTO.getPhone())
                                .email(studentDTO.getEmail())
                                .address(studentDTO.getAddress())
                                .educationLevel(studentDTO.getEducationLevel())
                                .build())
                        .collect(Collectors.toSet()))
                .build();
    }

    private GuardianDTO convertToGuardianDTOWithoutStudents(GuardianEntity guardianEntity) {
        return GuardianDTO.builder()
                .id(guardianEntity.getId())
                .name(guardianEntity.getName())
                .phone(guardianEntity.getPhone())
                .address(guardianEntity.getAddress())
                .studentDTOS(Collections.emptySet())
                .build();
    }
}
