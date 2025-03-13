package gt.com.megatech.service.implementation;

import gt.com.megatech.persistence.entity.PaymentEntity;
import gt.com.megatech.persistence.entity.StudentEntity;
import gt.com.megatech.persistence.entity.SuspendedStudentEntity;
import gt.com.megatech.persistence.entity.enums.AcademicStatusEnum;
import gt.com.megatech.persistence.entity.enums.MonthEnum;
import gt.com.megatech.persistence.repository.IPaymentRepository;
import gt.com.megatech.persistence.repository.IStudentRepository;
import gt.com.megatech.persistence.repository.ISuspendedStudentRepository;
import gt.com.megatech.presentation.dto.*;
import gt.com.megatech.service.exception.PaymentNotFoundException;
import gt.com.megatech.service.exception.StudentNotFoundException;
import gt.com.megatech.service.interfaces.IPaymentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PaymentServiceImplementation implements IPaymentService {

    private final IPaymentRepository iPaymentRepository;
    private final IStudentRepository iStudentRepository;
    private final ISuspendedStudentRepository iSuspendedStudentRepository;

    @Transactional(
            readOnly = true
    )
    @Override
    public List<PaymentDTO> findAllPayments() {
        return this.iPaymentRepository.findAll().
                stream()
                .map(this::convertToPaymentDTO)
                .toList();
    }

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
            List<SuspendedStudentEntity> suspensions = iSuspendedStudentRepository.findByStudentEntity(student);
            for (YearMonth yearMonth = enrollmentYearMonth; yearMonth.compareTo(lastCompleteYearMonth) <= 0; yearMonth = yearMonth.plusMonths(1)) {
                MonthEnum monthEnum = MonthEnum.valueOf(yearMonth.getMonth().name());
                int year = yearMonth.getYear();
                YearMonth finalYearMonth = yearMonth;
                boolean isSuspended = suspensions.stream().anyMatch(suspension ->
                        isWithinSuspensionPeriod(
                                finalYearMonth,
                                suspension.getSuspensionDate(),
                                suspension.getReentryDate())
                );
                if (!isSuspended) {
                    boolean paymentExists = iPaymentRepository.existsByStudentEntityAndMonthEnumAndYear(student, monthEnum, year);
                    if (!paymentExists) {
                        lateMonths.add(yearMonth.getMonth().name() + " " + year);
                    }
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
    public Page<StudentLateDTO> findAllStudentsWithLatePayments(Pageable pageable) {
        LocalDate now = LocalDate.now();
        YearMonth lastCompleteYearMonth = YearMonth.from(now).minusMonths(1);
        LocalDate targetDate = lastCompleteYearMonth.atEndOfMonth();
        Page<StudentEntity> studentPage = iStudentRepository.findAllStudentsWithLatePayments(
                AcademicStatusEnum.STUDYING,
                targetDate,
                pageable
        );
        List<StudentLateDTO> studentLateDTOs = studentPage.stream()
                .map(student -> {
                    LocalDate enrollmentDate = student.getEnrollmentEntity().getEnrollmentDate();
                    YearMonth enrollmentYearMonth = YearMonth.from(enrollmentDate);
                    List<String> lateMonths = new ArrayList<>();
                    List<SuspendedStudentEntity> suspensions = iSuspendedStudentRepository.findByStudentEntity(student);
                    for (YearMonth yearMonth = enrollmentYearMonth; yearMonth.compareTo(lastCompleteYearMonth) <= 0; yearMonth = yearMonth.plusMonths(1)) {
                        MonthEnum monthEnum = MonthEnum.valueOf(yearMonth.getMonth().name());
                        int year = yearMonth.getYear();
                        YearMonth finalYearMonth = yearMonth;
                        boolean isSuspended = suspensions.stream().anyMatch(suspension ->
                                isWithinSuspensionPeriod(
                                        finalYearMonth,
                                        suspension.getSuspensionDate(),
                                        suspension.getReentryDate())
                        );
                        if (!isSuspended) {
                            boolean paymentExists = iPaymentRepository.existsByStudentEntityAndMonthEnumAndYear(student, monthEnum, year);
                            if (!paymentExists) {
                                lateMonths.add(yearMonth.getMonth().name() + " " + year);
                            }
                        }
                    }
                    if (!lateMonths.isEmpty()) {
                        String message = "Student has late payments for the following months: " + String.join(", ", lateMonths);
                        StudentLateDTO dto = convertToStudentLateDTO(student);
                        dto.setLateMonths(lateMonths);
                        dto.setLatePaymentMessage(message);
                        return dto;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
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

    @Transactional(
            readOnly = true
    )
    @Override
    public List<PaymentDTO> findAllPaymentsByStudentId(
            Long studentId
    ) {
        return this.iPaymentRepository.findByStudentEntity_Id(studentId)
                .stream()
                .map(this::convertToPaymentDTO)
                .toList();
    }

    @Transactional
    @Override
    public List<PaymentDTO> savePayments(PaymentRequestDTO paymentRequestDTO) {
        StudentEntity studentEntity = this.iStudentRepository.findById(
                paymentRequestDTO.getStudentId()
        ).orElseThrow(() -> new StudentNotFoundException(paymentRequestDTO.getStudentId()));
        if (!studentEntity.getAcademicStatusEnum().equals(AcademicStatusEnum.STUDYING)) {
            throw new IllegalStateException("Only students with STUDYING status can make payments.");
        }
        if (paymentRequestDTO.getPaymentDetailDTOS().size() > 24) {
            throw new IllegalArgumentException("Cannot pay for more than 24 months.");
        }
        for (PaymentDetailDTO paymentDetailDTO : paymentRequestDTO.getPaymentDetailDTOS()) {
            boolean exists = this.iPaymentRepository.existsByStudentEntityAndMonthEnumAndYear(
                    studentEntity,
                    paymentDetailDTO.getMonthEnum(),
                    paymentDetailDTO.getYear()
            );
            if (exists) {
                throw new IllegalStateException("Payment for "
                        + paymentDetailDTO.getMonthEnum()
                        + " "
                        + paymentDetailDTO.getYear()
                        + " already exists.");
            }
        }
        List<PaymentDetailDTO> paymentDetails = paymentRequestDTO.getPaymentDetailDTOS();
        if (paymentDetails.size() > 1) {
            List<String> monthNames = new ArrayList<>();
            for (int i = 0; i < paymentDetails.size(); i++) {
                PaymentDetailDTO detail = paymentDetails.get(i);
                monthNames.add(detail.getMonthEnum().name());
                if (i < paymentDetails.size() - 1) {
                    detail.setAmountPaid(BigDecimal.ZERO);
                    detail.setLateFee(BigDecimal.ZERO);
                    detail.setNotes("PAGO");
                } else {
                    detail.setNotes("PAGO, " + String.join(", ", monthNames));
                }
            }
        }
        List<PaymentEntity> paymentEntityList = convertToPaymentEntities(paymentRequestDTO, studentEntity);
        List<PaymentEntity> savedPayments = this.iPaymentRepository.saveAll(paymentEntityList);
        return savedPayments.stream()
                .map(this::convertToPaymentDTO)
                .toList();
    }

    @Transactional
    @Override
    public PaymentDTO updatePayment(Long paymentId, PaymentRequestDTO updatedPaymentRequest) {
        PaymentEntity paymentEntity = this.iPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment with ID " + paymentId + " not found"));
        if (!paymentEntity.getStudentEntity().getAcademicStatusEnum().equals(AcademicStatusEnum.STUDYING)) {
            throw new IllegalStateException("Only students with STUDYING status can update payments.");
        }
        if (updatedPaymentRequest.getPaymentDetailDTOS().isEmpty()) {
            throw new IllegalArgumentException("Payment details cannot be empty.");
        }
        PaymentDetailDTO updatedPaymentDetail = updatedPaymentRequest.getPaymentDetailDTOS().get(0);
        boolean exists = this.iPaymentRepository.existsByStudentEntityAndMonthEnumAndYear(
                paymentEntity.getStudentEntity(), updatedPaymentDetail.getMonthEnum(), updatedPaymentDetail.getYear()
        );
        if (exists && (!paymentEntity.getMonthEnum().equals(updatedPaymentDetail.getMonthEnum()) ||
                !Objects.equals(paymentEntity.getYear(), updatedPaymentDetail.getYear()))) {
            throw new IllegalStateException("A payment for " + updatedPaymentDetail.getMonthEnum() + " " +
                    updatedPaymentDetail.getYear() + " already exists.");
        }
        paymentEntity.setAmountPaid(updatedPaymentDetail.getAmountPaid());
        paymentEntity.setMonthEnum(updatedPaymentDetail.getMonthEnum());
        paymentEntity.setYear(updatedPaymentDetail.getYear());
        paymentEntity.setPaymentDate(updatedPaymentDetail.getPaymentDate());
        PaymentEntity updatedPayment = this.iPaymentRepository.save(paymentEntity);
        return convertToPaymentDTO(updatedPayment);
    }

    private boolean isWithinSuspensionPeriod(
            YearMonth yearMonth,
            LocalDate suspensionDate,
            LocalDate reentryDate
    ) {
        YearMonth suspensionYM = YearMonth.from(
                suspensionDate
        );
        YearMonth reentryYM = (reentryDate != null) ?
                YearMonth.from(reentryDate) :
                YearMonth.now().plusMonths(1);
        return (yearMonth.compareTo(suspensionYM) >= 0) && (yearMonth.compareTo(reentryYM) < 0);
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
