package gt.com.megatech.service.interfaces;

import gt.com.megatech.persistence.entity.enums.MonthEnum;
import gt.com.megatech.presentation.dto.PaymentDTO;
import gt.com.megatech.presentation.dto.PaymentRequestDTO;
import gt.com.megatech.presentation.dto.StudentDTO;
import gt.com.megatech.presentation.dto.StudentLateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPaymentService {

    List<PaymentDTO> findAllPaymentsByMonthAndYear(
            MonthEnum monthEnum,
            Integer year
    );

    Page<PaymentDTO> findAllPaymentsByMonthAndYear(
            MonthEnum monthEnum,
            Integer year,
            Pageable pageable
    );

    List<StudentDTO> findAllStudentsWithPendingPayments();

    Page<StudentDTO> findAllStudentsWithPendingPayments(
            Pageable pageable
    );

    List<StudentLateDTO> findAllStudentsWithLatePayments();

    Page<StudentLateDTO> findAllStudentsWithLatePayments(
            Pageable pageable
    );

    PaymentDTO findByIdPayment(
            Long id
    );

    List<PaymentDTO> savePayments(
            PaymentRequestDTO paymentRequestDTO
    );

    PaymentDTO updatePayment(
            Long paymentId,
            PaymentRequestDTO updatedPaymentDetail
    );
}
