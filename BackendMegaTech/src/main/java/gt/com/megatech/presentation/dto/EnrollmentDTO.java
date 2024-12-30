package gt.com.megatech.presentation.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentDTO {

    private Long id;
    private LocalDate enrollmentDate;
    private BigDecimal paymentAmount;
    private StudentDTO studentDTO;
}
