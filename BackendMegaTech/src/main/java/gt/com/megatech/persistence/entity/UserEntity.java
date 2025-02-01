package gt.com.megatech.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
@Table(
        name = "users"
)
public class UserEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_seq"
    )
    @SequenceGenerator(
            name = "user_seq",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    private Long id;

    @NotBlank(
            message = "Username cannot be empty"
    )
    @Size(
            min = 5,
            max = 100,
            message = "The username must be between 5 and 100 characters long."
    )
    @Column(
            length = 100,
            nullable = false,
            unique = true
    )
    private String username;

    @NotBlank(
            message = "Password cannot be empty"
    )
    @Size(
            min = 8,
            max = 64,
            message = "Password must be between 8 and 64 characters long"
    )
    @Column(
            nullable = false
    )
    private String password;

    @Builder.Default
    @Column(
            name = "is_enabled",
            nullable = false
    )
    private boolean isEnabled = true;

    @Builder.Default
    @Column(
            name = "is_account_non_expired",
            nullable = false
    )
    private boolean isAccountNonExpired = true;

    @Builder.Default
    @Column(
            name = "is_account_non_locked",
            nullable = false
    )
    private boolean isAccountNonLocked = true;

    @Builder.Default
    @Column(
            name = "is_credentials_non_expired",
            nullable = false
    )
    private boolean isCredentialsNonExpired = true;

    @NotEmpty(
            message = "The user must have at least one role"
    )
    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(
                    name = "user_id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id"
            )
    )
    private Set<RoleEntity> roles = new HashSet<>();
}
