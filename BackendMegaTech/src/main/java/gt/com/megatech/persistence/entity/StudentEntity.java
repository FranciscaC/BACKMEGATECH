package gt.com.megatech.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "students")
public class StudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_seq")
    @SequenceGenerator(name = "student_seq", sequenceName = "student_sequence", allocationSize = 1)
    private Long id;

    @NotBlank(message = "The name must not be empty")
    @Size(min = 10, max = 100, message = "The name must be between 10 and 100 characters long.")
    @Column(length = 100, nullable = false, unique = true)
    private String name;

    @Past(message = "The birth date must be a date in the past.")
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @NotBlank(message = "The phone must not be empty")
    @Size(max = 8, message = "The phone number must be exactly 8 characters long.")
    @Pattern(regexp = "\\d{8}", message = "The phone number must contain exactly 8 digits.")
    @Column(length = 8, nullable = false, unique = true)
    private String phone;

    @NotBlank(message = "The email must not be empty.")
    @Email(message = "The email must be valid.")
    @Size(max = 100, message = "The email must not exceed 100 characters.")
    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @NotBlank(message = "The address must not be empty")
    @Size(min = 10, max = 200, message = "The address must be between 10 and 200 characters.")
    @Column(length = 200, nullable = false)
    private String address;

    @NotBlank(message = "The education level must not be empty.")
    @Size(min = 5, max = 50, message = "The education level must be between 5 and 50 characters.")
    @Column(name = "education_level", length = 50, nullable = false)
    private String educationLevel;

    @NotNull(message = "The student must have a guardian")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guardian_id", nullable = false)
    private GuardianEntity guardianEntity;

    @OneToOne(mappedBy = "studentEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private EnrollmentEntity enrollmentEntity;
}
