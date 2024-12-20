package gt.com.megatech.presentation.controller;

import gt.com.megatech.presentation.dto.GuardianDTO;
import gt.com.megatech.service.assembler.GuardianModelAssembler;
import gt.com.megatech.service.interfaces.IGuardianService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/guardian")
@RequiredArgsConstructor
@PreAuthorize("denyAll()")
public class GuardianController {

    private final GuardianModelAssembler guardianModelAssembler;
    private final IGuardianService iGuardianService;

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping
    public CollectionModel<EntityModel<GuardianDTO>> findAllGuardians() {
        List<EntityModel<GuardianDTO>> guardians = this.iGuardianService.findAllGuardians()
                .stream()
                .map(guardianModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(
                guardians,
                linkTo(methodOn(GuardianController.class).findAllGuardians()).withSelfRel()
        );
    }

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping("/with-students")
    public CollectionModel<EntityModel<GuardianDTO>> findAllGuardiansWithStudents() {
        List<EntityModel<GuardianDTO>> guardians = this.iGuardianService.findAllGuardiansWithStudents()
                .stream()
                .map(guardianModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(
                guardians,
                linkTo(methodOn(GuardianController.class).findAllGuardiansWithStudents()).withSelfRel()
        );
    }

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping("/{id}")
    public EntityModel<GuardianDTO> findGuardianById(@PathVariable Long id) {
        GuardianDTO guardian = this.iGuardianService.findByIdGuardian(id);
        return guardianModelAssembler.toModel(guardian);
    }

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping("/{id}/with-students")
    public EntityModel<GuardianDTO> findGuardianByIdWithStudents(@PathVariable Long id) {
        GuardianDTO guardian = this.iGuardianService.findGuardianByIdWithStudents(id);
        return guardianModelAssembler.toModel(guardian);
    }

    @PreAuthorize("hasAuthority('CREATE')")
    @PostMapping
    public ResponseEntity<?> saveGuardian(@RequestBody @Valid GuardianDTO guardianDTO) {
        EntityModel<GuardianDTO> guardianDTOEntityModel = guardianModelAssembler
                .toModel(this.iGuardianService.saveGuardian(guardianDTO));
        return ResponseEntity
                .created(guardianDTOEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(guardianDTOEntityModel);
    }

    @PreAuthorize("hasAuthority('UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGuardian(@PathVariable Long id, @RequestBody @Valid GuardianDTO guardianDTO) {
        EntityModel<GuardianDTO> guardianDTOEntityModel = guardianModelAssembler
                .toModel(this.iGuardianService.updateGuardian(id, guardianDTO));
        return ResponseEntity
                .created(guardianDTOEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(guardianDTOEntityModel);
    }

    @PreAuthorize("hasAuthority('DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuardian(@PathVariable Long id) {
        this.iGuardianService.deleteGuardian(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
