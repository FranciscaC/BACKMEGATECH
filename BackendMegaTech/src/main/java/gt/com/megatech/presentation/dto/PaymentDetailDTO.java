package gt.com.megatech.presentation.dto;

import gt.com.megatech.persistence.entity.enums.MonthEnum;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDetailDTO {

    private MonthEnum monthEnum;
    private Integer year;
    private BigDecimal amountPaid;
    private LocalDate paymentDate;
    private BigDecimal lateFee;
    private String notes;
}
