package gt.com.megatech.persistence.repository;

import gt.com.megatech.persistence.entity.GraduatedStudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IGraduatedStudentRepository extends JpaRepository<GraduatedStudentEntity, Long> {
}
