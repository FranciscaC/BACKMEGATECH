package gt.com.megatech.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import gt.com.megatech.persistence.entity.enums.AcademicStatusEnum;
import gt.com.megatech.persistence.entity.enums.EducationLevelEnum;
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
    private EducationLevelEnum educationLevelEnum;
    private AcademicStatusEnum academicStatusEnum;
    private GuardianDTO guardianDTO;
}
