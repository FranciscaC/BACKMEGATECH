package gt.com.megatech.presentation.dto;

import jakarta.validation.Valid;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDTO {

    private Long studentId;
    private List<@Valid PaymentDetailDTO> paymentDetailDTOS = new ArrayList<>();
}
