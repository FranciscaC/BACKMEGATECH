package gt.com.megatech.persistence.repository;

import gt.com.megatech.persistence.entity.StudentEntity;
import gt.com.megatech.persistence.entity.enums.AcademicStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IStudentRepository extends JpaRepository<StudentEntity, Long> {

    boolean existsByName(String name);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    List<StudentEntity> findByAcademicStatusEnum(AcademicStatusEnum academicStatusEnum);

    Page<StudentEntity> findByAcademicStatusEnum(AcademicStatusEnum academicStatusEnum, Pageable pageable);

    @Modifying
    @Query("UPDATE StudentEntity s SET s.academicStatusEnum = :status WHERE s.id = :id")
    int updateAcademicStatusById(@Param("id") Long id, @Param("status") AcademicStatusEnum status);
}
