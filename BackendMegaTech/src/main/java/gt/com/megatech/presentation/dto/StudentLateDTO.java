package gt.com.megatech.presentation.dto;

import gt.com.megatech.persistence.entity.enums.AcademicStatusEnum;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentLateDTO {

    private Long id;
    private String name;
    private String cui;
    private String personalCode;
    private LocalDate birthDate;
    private String phone;
    private String email;
    private String address;
    private String educationLevel;
    private AcademicStatusEnum academicStatusEnum;
    private String latePaymentMessage;
}
