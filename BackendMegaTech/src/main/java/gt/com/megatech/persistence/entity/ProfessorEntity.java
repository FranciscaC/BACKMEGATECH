package gt.com.megatech.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "professors")
public class ProfessorEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "professor_seq"
    )
    @SequenceGenerator(
            name = "professor_seq",
            sequenceName = "professor_sequence",
            allocationSize = 1
    )
    private Long id;

    @NotBlank(
            message = "The name must not be empty"
    )
    @Size(
            min = 10,
            max = 100,
            message = "The name must be between 10 and 100 characters long."
    )
    @Column(
            length = 100,
            nullable = false,
            unique = true
    )
    private String name;

    @NotBlank(
            message = "The phone must not be empty"
    )
    @Size(
            max = 8,
            message = "The phone number must be exactly 8 characters long."
    )
    @Pattern(
            regexp = "\\d{8}",
            message = "The phone number must contain exactly 8 digits."
    )
    @Column(
            length = 8,
            nullable = false,
            unique = true
    )
    private String phone;

    @NotBlank(
            message = "The email must not be empty."
    )
    @Email(
            message = "The email must be valid."
    )
    @Size(
            max = 100,
            message = "The email must not exceed 100 characters."
    )
    @Column(
            length = 100,
            nullable = false,
            unique = true
    )
    private String email;

    @NotBlank(
            message = "The address must not be empty"
    )
    @Size(
            min = 10,
            max = 200,
            message = "The address must be between 10 and 200 characters."
    )
    @Column(
            length = 200,
            nullable = false
    )
    private String address;
}
