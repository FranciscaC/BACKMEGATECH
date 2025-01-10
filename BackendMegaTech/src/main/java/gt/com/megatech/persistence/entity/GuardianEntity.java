package gt.com.megatech.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "guardians")
public class GuardianEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "guardian_seq"
    )
    @SequenceGenerator(
            name = "guardian_seq",
            sequenceName = "guardian_sequence",
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
            message = "The address must not be empty"
    )
    @Size(
            min = 5,
            max = 200,
            message = "The address must be between 10 and 200 characters."
    )
    @Column(
            length = 200,
            nullable = false
    )
    private String address;

    @OneToMany(
            mappedBy = "guardianEntity",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private Set<StudentEntity> studentEntities = new HashSet<>();
}
