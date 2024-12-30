package gt.com.megatech.presentation.controller;

import gt.com.megatech.presentation.dto.AuthLoginRequestDTO;
import gt.com.megatech.presentation.dto.AuthResponseDTO;
import gt.com.megatech.service.implementation.UserDetailServiceImplementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/authentication")
@PreAuthorize("permitAll()")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserDetailServiceImplementation userDetailServiceImplementation;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthLoginRequestDTO authLoginRequestDTO) {
        return new ResponseEntity<>(
                this.userDetailServiceImplementation.loginUser(authLoginRequestDTO),
                HttpStatus.OK
        );
    }
}
