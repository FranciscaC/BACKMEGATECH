package gt.com.megatech.presentation.controller;

import gt.com.megatech.presentation.dto.AuthCreateUserRequestDTO;
import gt.com.megatech.presentation.dto.AuthResponseDTO;
import gt.com.megatech.persistence.entity.UserEntity;
import gt.com.megatech.service.implementation.UserDetailServiceImplementation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
        "/api/user"
)
@RequiredArgsConstructor
@PreAuthorize(
        "denyAll()"
)
public class UserController {

    private final UserDetailServiceImplementation userDetailServiceImplementation;

    @PreAuthorize(
            "hasAuthority('READ')"
    )
    @GetMapping
    public ResponseEntity<List<UserEntity>> findAllUsers() {
        return new ResponseEntity<>(
                this.userDetailServiceImplementation.findAllUsers(),
                HttpStatus.OK
        );
    }

    @PreAuthorize(
            "hasAuthority('READ')"
    )
    @GetMapping(
            "/paged"
    )
    public ResponseEntity<Page<UserEntity>> findAllUsersPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Pageable pageable
    ) {
        Pageable customPageable = PageRequest.of(
                page,
                size,
                pageable.getSort()
        );
        Page<UserEntity> usersPage = userDetailServiceImplementation.findAllUsersPaged(
                customPageable
        );
        return new ResponseEntity<>(
                usersPage,
                HttpStatus.OK
        );
    }

    @PreAuthorize(
            "hasAuthority('CREATE')"
    )
    @PostMapping
    public ResponseEntity<AuthResponseDTO> register(
            @RequestBody @Valid AuthCreateUserRequestDTO authCreateUserRequestDTO
    ) {
        return new ResponseEntity<>(
                this.userDetailServiceImplementation.createUser(
                        authCreateUserRequestDTO
                ),
                HttpStatus.CREATED
        );
    }

    @PreAuthorize(
            "hasAuthority('UPDATE')"
    )
    @PutMapping(
            "/{username}"
    )
    public ResponseEntity<AuthResponseDTO> updateUser(
            @PathVariable String username,
            @RequestBody @Valid AuthCreateUserRequestDTO authCreateUserRequestDTO
    ) {
        return new ResponseEntity<>(
                this.userDetailServiceImplementation.updateUser(
                        username,
                        authCreateUserRequestDTO
                ),
                HttpStatus.OK
        );
    }

    @PreAuthorize(
            "hasAuthority('DELETE')"
    )
    @DeleteMapping(
            "/{username}"
    )
    public ResponseEntity<Void> deleteUser(
            @PathVariable String username
    ) {
        this.userDetailServiceImplementation.deleteUser(
                username
        );
        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT
        );
    }
}
