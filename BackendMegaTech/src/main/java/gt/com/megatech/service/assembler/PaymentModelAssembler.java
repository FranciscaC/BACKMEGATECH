package gt.com.megatech.service.assembler;

import gt.com.megatech.persistence.entity.enums.MonthEnum;
import gt.com.megatech.presentation.controller.PaymentController;
import gt.com.megatech.presentation.dto.PaymentDTO;
import gt.com.megatech.presentation.dto.StudentDTO;
import gt.com.megatech.presentation.dto.StudentLateDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PaymentModelAssembler implements RepresentationModelAssembler<PaymentDTO, EntityModel<PaymentDTO>> {

    @Override
    public @NonNull EntityModel<PaymentDTO> toModel(
            @NonNull PaymentDTO paymentDTO
    ) {
        return EntityModel.of(
                paymentDTO,
                linkTo(methodOn(PaymentController.class)
                        .findByIdPayment(paymentDTO.getId()))
                        .withSelfRel(),
                linkTo(methodOn(PaymentController.class)
                        .findAllPaymentsByMonthAndYear(
                                paymentDTO.getMonthEnum(),
                                paymentDTO.getYear()
                        ))
                        .withRel("payment")
        );
    }

    public @NonNull EntityModel<StudentDTO> toModelForPendingPayment(
            @NonNull StudentDTO studentDTO
    ) {
        return EntityModel.of(
                studentDTO,
                linkTo(methodOn(PaymentController.class).findAllStudentsWithPendingPayments())
                        .withSelfRel()
        );
    }

    public @NonNull EntityModel<StudentLateDTO> toModelForLatePayment(
            @NonNull StudentLateDTO studentLateDTO
    ) {
        return EntityModel.of(
                studentLateDTO,
                linkTo(methodOn(PaymentController.class).findAllStudentsWithLatePayments())
                        .withSelfRel()
        );
    }
}
