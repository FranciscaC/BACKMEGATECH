package gt.com.megatech.presentation.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfessorDTO {

    private Long id;
    private String name;
    private String phone;
    private String email;
    private String address;
}
