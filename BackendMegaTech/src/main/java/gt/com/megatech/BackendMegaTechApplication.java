package gt.com.megatech;

import gt.com.megatech.persistence.entity.PermissionEntity;
import gt.com.megatech.persistence.entity.RoleEntity;
import gt.com.megatech.persistence.entity.RoleEnum;
import gt.com.megatech.persistence.entity.UserEntity;
import gt.com.megatech.persistence.repository.IUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootApplication
public class BackendMegaTechApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendMegaTechApplication.class, args);
    }

    @Bean
    CommandLineRunner init(IUserRepository iUserRepository) {
        return args -> {
            // Creating permissions
            PermissionEntity readPermission = PermissionEntity
                    .builder()
                    .name("READ")
                    .build();
            PermissionEntity createPermission = PermissionEntity
                    .builder()
                    .name("CREATE")
                    .build();
            PermissionEntity updatePermission = PermissionEntity
                    .builder()
                    .name("UPDATE")
                    .build();
            PermissionEntity deletePermission = PermissionEntity
                    .builder()
                    .name("DELETE")
                    .build();

            // Creating roles
            RoleEntity roleAdmin = RoleEntity
                    .builder()
                    .roleEnum(RoleEnum.ADMIN)
                    .permissionList(Set.of(readPermission, createPermission, updatePermission, deletePermission))
                    .build();
            RoleEntity roleUser = RoleEntity
                    .builder()
                    .roleEnum(RoleEnum.USER)
                    .permissionList(Set.of(readPermission, createPermission))
                    .build();

            // Creating users
            UserEntity admin = UserEntity
                    .builder()
                    .username("mega_tech_admin")
                    .password("$2a$10$Oqq9ZVPOTxsmez/BlLNRbuekv8Oe.Gdbv5A9HMhPym.5ogNSClUtm")
                    .roles(Set.of(roleAdmin))
                    .build();
            UserEntity user = UserEntity
                    .builder()
                    .username("mega_tech_user")
                    .password("$2a$10$Oqq9ZVPOTxsmez/BlLNRbuekv8Oe.Gdbv5A9HMhPym.5ogNSClUtm")
                    .roles(Set.of(roleUser))
                    .build();

            // Check if users exist
            Optional<UserEntity> existingAdmin = iUserRepository.findUserEntityByUsername("mega_tech_admin");
            Optional<UserEntity> existingUser = iUserRepository.findUserEntityByUsername("mega_tech_user");

            if (existingAdmin.isEmpty() && existingUser.isEmpty()) {
                iUserRepository.saveAll(List.of(admin, user));
            }
        };
    }
}
