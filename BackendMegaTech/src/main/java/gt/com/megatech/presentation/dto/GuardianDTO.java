package gt.com.megatech.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(
        JsonInclude.Include.NON_EMPTY
)
public class GuardianDTO {

    private Long id;
    private String name;
    private String dpi;
    private String phone;
    private String address;
    private Set<StudentDTO> studentDTOS = new HashSet<>();
}
