package gt.com.megatech.persistence.repository;

import gt.com.megatech.persistence.entity.SuspendedStudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISuspendedStudentRepository extends JpaRepository<SuspendedStudentEntity, Long> {
}
