package gt.com.megatech.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StudentDTO {

    private Long id;
    private String name;
    private LocalDate birthDate;
    private String phone;
    private String email;
    private String address;
    private String educationLevel;
    private GuardianDTO guardianDTO;
}
