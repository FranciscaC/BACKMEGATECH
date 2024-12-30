package gt.com.megatech.persistence.repository;

import gt.com.megatech.persistence.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IStudentRepository extends JpaRepository<StudentEntity, Long> {

    boolean existsByName(String name);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);
}
