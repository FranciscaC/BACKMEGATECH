package gt.com.megatech.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "permissions"
)
public class PermissionEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "permission_seq"
    )
    @SequenceGenerator(
            name = "permission_seq",
            sequenceName = "permission_sequence",
            allocationSize = 1
    )
    private Long id;

    @NotBlank(
            message = "The permission name cannot be blank."
    )
    @Size(
            min = 4,
            max = 10,
            message = "The permission name must be between 4 and 10 characters."
    )
    @Column(
            nullable = false,
            unique = true,
            updatable = false
    )
    private String name;
}
