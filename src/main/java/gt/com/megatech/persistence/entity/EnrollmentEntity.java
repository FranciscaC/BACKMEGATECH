package gt.com.megatech.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
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
        name = "enrollments"
)
public class EnrollmentEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "enrollment_seq"
    )
    @SequenceGenerator(
            name = "enrollment_seq",
            sequenceName = "enrollment_sequence",
            allocationSize = 1
    )
    private Long id;

    @NotNull(
            message = "The enrollment date must not be empty"
    )
    @PastOrPresent(
            message = "The enrollment date must be in the past or present"
    )
    @Column(
            name = "enrollment_date",
            nullable = false
    )
    private LocalDate enrollmentDate;

    @NotNull(
            message = "The payment amount must not be empty"
    )
    @Column(
            name = "payment_amount",
            nullable = false
    )
    private BigDecimal paymentAmount;

    @NotNull(
            message = "The enrollment must be associated with a student"
    )
    @OneToOne(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "student_id",
            nullable = false,
            unique = true
    )
    private StudentEntity studentEntity;
}
