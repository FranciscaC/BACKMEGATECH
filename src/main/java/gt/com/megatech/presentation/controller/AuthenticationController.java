package gt.com.megatech.presentation.controller;

import gt.com.megatech.presentation.dto.AuthLoginRequestDTO;
import gt.com.megatech.presentation.dto.AuthResponseDTO;
import gt.com.megatech.presentation.dto.UserProfileResponseDTO;
import gt.com.megatech.service.implementation.UserDetailServiceImplementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(
        "/authentication"
)
@PreAuthorize(
        "permitAll()"
)
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserDetailServiceImplementation userDetailServiceImplementation;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody @Valid AuthLoginRequestDTO authLoginRequestDTO
    ) {
        return new ResponseEntity<>(
                this.userDetailServiceImplementation.loginUser(
                        authLoginRequestDTO
                ),
                HttpStatus.OK
        );
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN', 'USER')"
    )
    @GetMapping(
            "/me"
    )
    public ResponseEntity<UserProfileResponseDTO> getUserProfile(
            @RequestHeader("Authorization") String token
    ) {
        UserProfileResponseDTO userProfile = userDetailServiceImplementation.getUserProfileFromToken(
                token
        );
        return new ResponseEntity<>(
                userProfile,
                HttpStatus.OK
        );
    }
}
