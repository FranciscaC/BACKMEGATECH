package gt.com.megatech.persistence.repository;

import gt.com.megatech.persistence.entity.StudentEntity;
import gt.com.megatech.persistence.entity.SuspendedStudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ISuspendedStudentRepository extends JpaRepository<SuspendedStudentEntity, Long> {

    List<SuspendedStudentEntity> findByStudentEntity(StudentEntity studentEntity);
}
