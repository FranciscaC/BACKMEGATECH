package gt.com.megatech.persistence.entity;

import gt.com.megatech.persistence.entity.enums.MonthEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "payments"
)
public class PaymentEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "payment_seq"
    )
    @SequenceGenerator(
            name = "payment_seq",
            sequenceName = "payment_sequence",
            allocationSize = 1
    )
    private Long id;

    @Enumerated(
            EnumType.STRING
    )
    @NotNull(
            message = "Month cannot be null"
    )
    @Column(
            name = "month",
            nullable = false
    )
    private MonthEnum monthEnum;

    @NotNull(
            message = "Year cannot be null"
    )
    @Min(
            value = 2000,
            message = "Year must be at least 2000"
    )
    @Max(
            value = 2100,
            message = "Year must be at most 2100"
    )
    @Column(
            name = "year",
            nullable = false
    )
    private Integer year;

    @NotNull(
            message = "Amount paid cannot be null"
    )
    @Digits(
            integer = 10,
            fraction = 2,
            message = "Amount paid must be a valid decimal value with up to 2 decimal places"
    )
    @DecimalMin(
            value = "0.00",
            message = "Amount paid must be at least 0.00"
    )
    @Column(
            name = "amount_paid",
            nullable = false,
            columnDefinition = "DECIMAL(10, 2) DEFAULT 0.00"
    )
    private BigDecimal amountPaid;

    @NotNull(
            message = "Payment date cannot be null"
    )
    @PastOrPresent(
            message = "Payment date must be in the past or present"
    )
    @Column(
            name = "payment_date",
            nullable = false
    )
    private LocalDate paymentDate;

    @NotNull(
            message = "Late fee cannot be null"
    )
    @Digits(
            integer = 10,
            fraction = 2,
            message = "Late fee must be a valid decimal value with up to 2 decimal places"
    )
    @DecimalMin(
            value = "0.00",
            message = "Late fee must be at least 0.00"
    )
    @Column(
            name = "late_fee",
            nullable = false,
            columnDefinition = "DECIMAL(10, 2) DEFAULT 0.00"
    )
    private BigDecimal lateFee;

    @Size(
            max = 255,
            message = "Notes cannot exceed 255 characters"
    )
    @Column(
            name = "notes",
            nullable = false
    )
    private String notes;

    @NotNull(
            message = "Student cannot be null"
    )
    @ManyToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "student_id",
            updatable = false
    )
    private StudentEntity studentEntity;
}
