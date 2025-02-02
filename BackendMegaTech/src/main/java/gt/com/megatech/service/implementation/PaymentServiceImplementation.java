package gt.com.megatech.service.implementation;

import gt.com.megatech.persistence.entity.PaymentEntity;
import gt.com.megatech.persistence.entity.StudentEntity;
import gt.com.megatech.persistence.entity.enums.AcademicStatusEnum;
import gt.com.megatech.persistence.entity.enums.MonthEnum;
import gt.com.megatech.persistence.repository.IPaymentRepository;
import gt.com.megatech.persistence.repository.IStudentRepository;
import gt.com.megatech.presentation.dto.*;
import gt.com.megatech.service.exception.PaymentNotFoundException;
import gt.com.megatech.service.exception.StudentNotFoundException;
import gt.com.megatech.service.interfaces.IPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImplementation implements IPaymentService {

    private final IPaymentRepository iPaymentRepository;
    private final IStudentRepository iStudentRepository;

    @Transactional(
            readOnly = true
    )
    @Override
    public List<PaymentDTO> findAllPaymentsByMonthAndYear(
            MonthEnum monthEnum,
            Integer year
    ) {
        return this.iPaymentRepository.findByMonthEnumAndYear(
                        monthEnum,
                        year
                )
                .stream()
                .map(this::convertToPaymentDTO)
                .toList();
    }

    @Transactional(
            readOnly = true
    )
    @Override
    public Page<PaymentDTO> findAllPaymentsByMonthAndYear(
            MonthEnum monthEnum,
            Integer year,
            Pageable pageable
    ) {
        return this.iPaymentRepository.findByMonthEnumAndYear(
                        monthEnum,
                        year,
                        pageable
                )
                .map(this::convertToPaymentDTO);
    }

    @Transactional(
            readOnly = true
    )
    @Override
    public List<StudentDTO> findAllStudentsWithPendingPayments() {
        YearMonth yearMonth = YearMonth.now();
        List<StudentEntity> studentEntityList = this.iStudentRepository.findAllStudentsWithPendingPayments(
                AcademicStatusEnum.STUDYING,
                MonthEnum.fromMonthValue(yearMonth.getMonthValue()),
                yearMonth.getYear()
        );
        return studentEntityList
                .stream()
                .map(this::convertToStudentDTO)
                .toList();
    }

    @Transactional(
            readOnly = true
    )
    @Override
    public Page<StudentDTO> findAllStudentsWithPendingPayments(
            Pageable pageable
    ) {
        YearMonth currentYearMonth = YearMonth.now();
        Page<StudentEntity> studentPage = iStudentRepository.findAllStudentsWithPendingPayments(
                AcademicStatusEnum.STUDYING,
                MonthEnum.fromMonthValue(currentYearMonth.getMonthValue()),
                currentYearMonth.getYear(),
                pageable
        );
        List<StudentDTO> studentDTOs = studentPage.stream()
                .map(this::convertToStudentDTO)
                .toList();
        return new PageImpl<>(
                studentDTOs,
                pageable,
                studentPage.getTotalElements()
        );
    }

    @Transactional(
            readOnly = true
    )
    @Override
    public List<StudentLateDTO> findAllStudentsWithLatePayments() {
        LocalDate now = LocalDate.now();
        YearMonth lastCompleteYearMonth = YearMonth.from(now).minusMonths(1);
        LocalDate targetDate = lastCompleteYearMonth.atEndOfMonth();
        List<StudentEntity> studentEntityList = iStudentRepository.findAllStudentsWithLatePayments(
                AcademicStatusEnum.STUDYING,
                targetDate
        );
        List<StudentLateDTO> result = new ArrayList<>();
        for (StudentEntity student : studentEntityList) {
            LocalDate enrollmentDate = student.getEnrollmentEntity().getEnrollmentDate();
            YearMonth enrollmentYearMonth = YearMonth.from(enrollmentDate);
            List<String> lateMonths = new ArrayList<>();
            for (YearMonth ym = enrollmentYearMonth; ym.compareTo(lastCompleteYearMonth) <= 0; ym = ym.plusMonths(1)) {
                MonthEnum monthEnum = MonthEnum.valueOf(ym.getMonth().name());
                int year = ym.getYear();
                boolean paymentExists = iPaymentRepository.existsByStudentEntityAndMonthEnumAndYear(student, monthEnum, year);
                if (!paymentExists) {
                    lateMonths.add(ym.getMonth().name() + " " + year);
                }
            }
            if (!lateMonths.isEmpty()) {
                String message = "Student has late payments for the following months: " + String.join(", ", lateMonths);
                StudentLateDTO dto = convertToStudentLateDTO(student);
                dto.setLateMonths(lateMonths);
                dto.setLatePaymentMessage(message);
                result.add(dto);
            }
        }
        return result;
    }

    @Transactional(
            readOnly = true
    )
    @Override
    public Page<StudentLateDTO> findAllStudentsWithLatePayments(
            MonthEnum monthEnum,
            Integer year,
            Pageable pageable
    ) {
        YearMonth requestedYearMonth = YearMonth.of(year, monthEnum.ordinal() + 1);
        LocalDate targetDate = requestedYearMonth.atEndOfMonth();
        if (YearMonth.now().isBefore(requestedYearMonth.plusMonths(1))) {
            throw new IllegalArgumentException("Cannot calculate late payments for a future month.");
        }
        Page<StudentEntity> studentPage = iStudentRepository.findAllStudentsWithLatePayments(
                AcademicStatusEnum.STUDYING,
                monthEnum,
                year,
                targetDate,
                pageable
        );
        List<StudentLateDTO> studentLateDTOs = studentPage.stream()
                .map(student -> {
                    StudentLateDTO dto = convertToStudentLateDTO(student);
                    dto.setLatePaymentMessage("Student has a late payment for " + monthEnum.name() + " " + year);
                    return dto;
                })
                .toList();
        return new PageImpl<>(
                studentLateDTOs,
                pageable,
                studentPage.getTotalElements()
        );
    }

    @Transactional(
            readOnly = true
    )
    @Override
    public PaymentDTO findByIdPayment(
            Long id
    ) {
        PaymentEntity paymentEntity = this.iPaymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException(id));
        return this.convertToPaymentDTO(paymentEntity);
    }

    @Transactional
    @Override
    public List<PaymentDTO> savePayments(
            PaymentRequestDTO paymentRequestDTO
    ) {
        StudentEntity studentEntity = this.iStudentRepository.findById(
                        paymentRequestDTO.getStudentId()
                )
                .orElseThrow(() -> new StudentNotFoundException(paymentRequestDTO.getStudentId()));
        if (!studentEntity.getAcademicStatusEnum().equals(AcademicStatusEnum.STUDYING)) {
            throw new IllegalStateException(
                    "Only students with STUDYING status can make payments."
            );
        }
        if (paymentRequestDTO.getPaymentDetailDTOS().size() > 24) {
            throw new IllegalArgumentException(
                    "Cannot pay for more than 24 months."
            );
        }
        for (PaymentDetailDTO paymentDetailDTO : paymentRequestDTO.getPaymentDetailDTOS()) {
            boolean exists = this.iPaymentRepository.existsByStudentEntityAndMonthEnumAndYear(
                    studentEntity, paymentDetailDTO.getMonthEnum(), paymentDetailDTO.getYear()
            );

            if (exists) {
                throw new IllegalStateException(
                        "Payment for "
                                + paymentDetailDTO.getMonthEnum()
                                + " "
                                + paymentDetailDTO.getYear()
                                + " already exists."
                );
            }
        }
        List<PaymentEntity> paymentEntityList = convertToPaymentEntities(
                paymentRequestDTO,
                studentEntity
        );
        List<PaymentEntity> savedPayments = this.iPaymentRepository.saveAll(
                paymentEntityList
        );
        return savedPayments.stream()
                .map(this::convertToPaymentDTO)
                .toList();
    }

    private PaymentDTO convertToPaymentDTO(
            PaymentEntity paymentEntity
    ) {
        return PaymentDTO.builder()
                .id(paymentEntity.getId())
                .monthEnum(paymentEntity.getMonthEnum())
                .year(paymentEntity.getYear())
                .amountPaid(paymentEntity.getAmountPaid())
                .paymentDate(paymentEntity.getPaymentDate())
                .lateFee(paymentEntity.getLateFee())
                .notes(paymentEntity.getNotes())
                .studentDTO(convertToStudentDTO(paymentEntity.getStudentEntity()))
                .build();
    }

    private List<PaymentEntity> convertToPaymentEntities(
            PaymentRequestDTO paymentRequestDTO, StudentEntity studentEntity
    ) {
        return paymentRequestDTO.getPaymentDetailDTOS()
                .stream()
                .map(paymentDetailDTO -> PaymentEntity.builder()
                        .studentEntity(studentEntity)
                        .monthEnum(paymentDetailDTO.getMonthEnum())
                        .year(paymentDetailDTO.getYear())
                        .amountPaid(paymentDetailDTO.getAmountPaid())
                        .paymentDate(paymentDetailDTO.getPaymentDate())
                        .lateFee(paymentDetailDTO.getLateFee())
                        .notes(paymentDetailDTO.getNotes())
                        .build()
                )
                .toList();
    }

    private StudentDTO convertToStudentDTO(
            StudentEntity studentEntity
    ) {
        return StudentDTO.builder()
                .id(studentEntity.getId())
                .name(studentEntity.getName())
                .cui(studentEntity.getCui())
                .personalCode(studentEntity.getPersonalCode())
                .birthDate(studentEntity.getBirthDate())
                .phone(studentEntity.getPhone())
                .email(studentEntity.getEmail())
                .address(studentEntity.getAddress())
                .educationLevel(studentEntity.getEducationLevel())
                .academicStatusEnum(studentEntity.getAcademicStatusEnum())
                .build();
    }

    private StudentLateDTO convertToStudentLateDTO(
            StudentEntity studentEntity
    ) {
        return StudentLateDTO.builder()
                .id(studentEntity.getId())
                .name(studentEntity.getName())
                .cui(studentEntity.getCui())
                .personalCode(studentEntity.getPersonalCode())
                .birthDate(studentEntity.getBirthDate())
                .phone(studentEntity.getPhone())
                .email(studentEntity.getEmail())
                .address(studentEntity.getAddress())
                .educationLevel(studentEntity.getEducationLevel())
                .academicStatusEnum(studentEntity.getAcademicStatusEnum())
                .latePaymentMessage("")
                .lateMonths(null)
                .build();
    }
}
