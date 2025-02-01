package gt.com.megatech.presentation.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuspendedStudentDTO {

    private Long id;
    private LocalDate suspensionDate;
    private LocalDate reentryDate;
    private StudentDTO studentDTO;
}
