package gt.com.megatech.persistence.entity;

import gt.com.megatech.persistence.entity.enums.MonthEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "graduated_students"
)
public class GraduatedStudentEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "graduated_student_seq"
    )
    @SequenceGenerator(
            name = "graduated_student_seq",
            sequenceName = "graduated_student_sequence",
            allocationSize = 1
    )
    private Long id;

    @NotNull(
            message = "Graduation month cannot be null"
    )
    @Enumerated(
            EnumType.STRING
    )
    @Column(
            name = "graduation_month",
            nullable = false
    )
    private MonthEnum graduationMonth;

    @NotNull(
            message = "Graduation year cannot be null"
    )
    @Column(
            name = "graduation_year",
            nullable = false
    )
    private Integer graduationYear;

    @NotNull(
            message = "Graduation date cannot be null"
    )
    @Column(
            name = "graduation_date",
            nullable = false
    )
    private LocalDate graduationDate;

    @NotNull(
            message = "Student cannot be null"
    )
    @OneToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "student_id",
            updatable = false
    )
    private StudentEntity studentEntity;
}
