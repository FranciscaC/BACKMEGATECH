package gt.com.megatech.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "suspended_students"
)
public class SuspendedStudentEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "suspended_student_seq"
    )
    @SequenceGenerator(
            name = "suspended_student_seq",
            sequenceName = "suspended_student_sequence",
            allocationSize = 1
    )
    private Long id;

    @NotNull(
            message = "Suspension date must not be null."
    )
    @PastOrPresent(
            message = "Suspension date must be in the past or present."
    )
    @Column(
            name = "suspension_date", nullable = false
    )
    private LocalDate suspensionDate;

    @PastOrPresent(
            message = "Reentry date must be in the past or present."
    )
    @Column(
            name = "reentry_date"
    )
    private LocalDate reentryDate;

    @NotNull(
            message = "The student must not be null."
    )
    @ManyToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "student_id", nullable = false
    )
    private StudentEntity studentEntity;
}
