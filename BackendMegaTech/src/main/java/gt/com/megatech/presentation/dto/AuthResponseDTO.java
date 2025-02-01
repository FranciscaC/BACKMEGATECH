package gt.com.megatech.presentation.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username", "message", "jwt", "status"})
public record AuthResponseDTO(
        String username,
        String message,
        String jwt,
        boolean status
) {

    public AuthResponseDTO {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        if (jwt == null || jwt.isEmpty()) {
            throw new IllegalArgumentException("JWT cannot be null or empty");
        }
    }
}
