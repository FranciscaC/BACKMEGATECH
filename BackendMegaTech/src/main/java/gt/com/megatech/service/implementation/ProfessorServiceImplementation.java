package gt.com.megatech.service.implementation;

import gt.com.megatech.persistence.entity.ProfessorEntity;
import gt.com.megatech.persistence.repository.IProfessorRepository;
import gt.com.megatech.presentation.dto.ProfessorDTO;
import gt.com.megatech.service.exception.ProfessorNotFoundException;
import gt.com.megatech.service.interfaces.IProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorServiceImplementation implements IProfessorService {

    private final IProfessorRepository iProfessorRepository;

    @Transactional(readOnly = true)
    @Override
    public List<ProfessorDTO> findAllProfessors() {
        return this.iProfessorRepository.findAll()
                .stream()
                .map(this::convertToProfessorDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProfessorDTO> findAllProfessorsPaged(Pageable pageable) {
        return this.iProfessorRepository.findAll(pageable)
                .map(this::convertToProfessorDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public ProfessorDTO findByIdProfessor(Long id) {
        ProfessorEntity professorEntityExists = this.iProfessorRepository.findById(id)
                .orElseThrow(() -> new ProfessorNotFoundException(id));
        return this.convertToProfessorDTO(professorEntityExists);
    }

    @Transactional
    @Override
    public ProfessorDTO saveProfessor(ProfessorDTO professorDTO) {
        if (this.iProfessorRepository.existsByName(professorDTO.getName())) {
            throw new IllegalArgumentException("A professor with this name already exists.");
        }
        if (this.iProfessorRepository.existsByPhone(professorDTO.getPhone())) {
            throw new IllegalArgumentException("A professor with this phone number already exists.");
        }
        if (this.iProfessorRepository.existsByEmail(professorDTO.getEmail())) {
            throw new IllegalArgumentException("A professor with this email already exists.");
        }
        ProfessorEntity professorEntity = this.convertToProfessorEntity(professorDTO);
        ProfessorEntity professorEntitySaved = this.iProfessorRepository.save(professorEntity);
        return this.convertToProfessorDTO(professorEntitySaved);
    }

    @Transactional
    @Override
    public ProfessorDTO updateProfessor(Long id, ProfessorDTO professorDTO) {
        ProfessorEntity professorEntityExists = this.iProfessorRepository.findById(id)
                .orElseThrow(() -> new ProfessorNotFoundException(id));
        professorEntityExists.setName(professorDTO.getName());
        professorEntityExists.setPhone(professorDTO.getPhone());
        professorEntityExists.setEmail(professorDTO.getEmail());
        professorEntityExists.setAddress(professorDTO.getAddress());
        ProfessorEntity professorEntityUpdated = this.iProfessorRepository.save(professorEntityExists);
        return this.convertToProfessorDTO(professorEntityUpdated);
    }

    @Transactional
    @Override
    public void deleteProfessor(Long id) {
        ProfessorEntity professorEntity = this.iProfessorRepository.findById(id)
                .orElseThrow(() -> new ProfessorNotFoundException(id));
        this.iProfessorRepository.delete(professorEntity);
    }

    private ProfessorDTO convertToProfessorDTO(ProfessorEntity professorEntity) {
        return ProfessorDTO.builder()
                .id(professorEntity.getId())
                .name(professorEntity.getName())
                .phone(professorEntity.getPhone())
                .email(professorEntity.getEmail())
                .address(professorEntity.getAddress())
                .build();
    }

    private ProfessorEntity convertToProfessorEntity(ProfessorDTO professorDTO) {
        return ProfessorEntity.builder()
                .name(professorDTO.getName())
                .phone(professorDTO.getPhone())
                .email(professorDTO.getEmail())
                .address(professorDTO.getAddress())
                .build();
    }
}
