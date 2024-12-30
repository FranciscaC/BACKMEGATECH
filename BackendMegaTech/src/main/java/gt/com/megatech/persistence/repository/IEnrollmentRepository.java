package gt.com.megatech.persistence.repository;

import gt.com.megatech.persistence.entity.EnrollmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEnrollmentRepository extends JpaRepository<EnrollmentEntity, Long> {
}
