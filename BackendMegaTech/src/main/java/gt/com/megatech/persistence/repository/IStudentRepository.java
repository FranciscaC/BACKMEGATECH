package gt.com.megatech.persistence.repository;

import gt.com.megatech.persistence.entity.GuardianEntity;
import gt.com.megatech.persistence.entity.StudentEntity;
import gt.com.megatech.persistence.entity.enums.AcademicStatusEnum;
import gt.com.megatech.persistence.entity.enums.MonthEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IStudentRepository extends JpaRepository<StudentEntity, Long> {

    boolean existsByName(
            String name
    );

    boolean existsByPhone(
            String phone
    );

    boolean existsByEmail(
            String email
    );

    List<StudentEntity> findByAcademicStatusEnum(
            AcademicStatusEnum academicStatusEnum
    );

    Page<StudentEntity> findByAcademicStatusEnum(
            AcademicStatusEnum academicStatusEnum,
            Pageable pageable
    );

    @Modifying
    @Query("""
            UPDATE StudentEntity s
            SET s.academicStatusEnum = :academicStatusEnum
            WHERE s.id = :id
            """)
    int updateAcademicStatusById(
            @Param("id") Long id,
            @Param("academicStatusEnum") AcademicStatusEnum academicStatusEnum
    );

    @Modifying
    @Query("""
                UPDATE StudentEntity s 
                SET s.name = :name,
                    s.cui = :cui,
                    s.personalCode = :personalCode,
                    s.birthDate = :birthDate,
                    s.phone = :phone,
                    s.email = :email,
                    s.address = :address,
                    s.educationLevel = :educationLevel,
                    s.academicStatusEnum = :academicStatusEnum,
                    s.guardianEntity = :guardianEntity
                WHERE s.id = :id
            """)
    int updateStudentById(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("cui") String cui,
            @Param("personalCode") String personalCode,
            @Param("birthDate") LocalDate birthDate,
            @Param("phone") String phone,
            @Param("email") String email,
            @Param("address") String address,
            @Param("educationLevel") String educationLevel,
            @Param("academicStatusEnum") AcademicStatusEnum academicStatusEnum,
            @Param("guardianEntity") GuardianEntity guardianEntity
    );

    @Query("""
             SELECT s FROM StudentEntity s
             WHERE s.academicStatusEnum = :academicStatusEnum
             AND NOT EXISTS (
                 SELECT p FROM PaymentEntity p
                 WHERE p.studentEntity = s
                 AND p.monthEnum = :monthEnum
                 AND p.year = :year
             )
            """)
    List<StudentEntity> findAllStudentsWithPendingPayments(
            @Param("academicStatusEnum") AcademicStatusEnum academicStatusEnum,
            @Param("monthEnum") MonthEnum monthEnum,
            @Param("year") int year
    );

    @Query("""
             SELECT s FROM StudentEntity s
             WHERE s.academicStatusEnum = :academicStatusEnum
             AND NOT EXISTS (
                 SELECT p FROM PaymentEntity p
                 WHERE p.studentEntity = s
                 AND p.monthEnum = :monthEnum
                 AND p.year = :year
             )
            """)
    Page<StudentEntity> findAllStudentsWithPendingPayments(
            @Param("academicStatusEnum") AcademicStatusEnum academicStatusEnum,
            @Param("monthEnum") MonthEnum monthEnum,
            @Param("year") int year,
            Pageable pageable
    );

    @Query("""
             SELECT s
              FROM StudentEntity s
              JOIN EnrollmentEntity e ON e.studentEntity = s
              WHERE s.academicStatusEnum = :academicStatusEnum
              AND NOT EXISTS (
                  SELECT p
                  FROM PaymentEntity p
                  WHERE p.studentEntity = s
                  AND p.monthEnum = :monthEnum
                  AND p.year = :year
              )
              AND e.enrollmentDate <= :targetDate
            """)
    List<StudentEntity> findAllStudentsWithLatePayments(
            @Param("academicStatusEnum") AcademicStatusEnum academicStatusEnum,
            @Param("monthEnum") MonthEnum monthEnum,
            @Param("year") int year,
            @Param("targetDate") LocalDate targetDate
    );

    @Query("""
            SELECT s
            FROM StudentEntity s
            JOIN EnrollmentEntity e ON e.studentEntity = s
            WHERE s.academicStatusEnum = :academicStatusEnum
            AND e.enrollmentDate <= :targetDate
            """)
    List<StudentEntity> findAllStudentsWithLatePayments(
            @Param("academicStatusEnum") AcademicStatusEnum academicStatusEnum,
            @Param("targetDate") LocalDate targetDate
    );

    @Query("""
            SELECT s
            FROM StudentEntity s
            JOIN EnrollmentEntity e ON e.studentEntity = s
            WHERE s.academicStatusEnum = :academicStatusEnum
            AND e.enrollmentDate <= :targetDate
            """)
    Page<StudentEntity> findAllStudentsWithLatePayments(
            @Param("academicStatusEnum") AcademicStatusEnum academicStatusEnum,
            @Param("targetDate") LocalDate targetDate,
            Pageable pageable
    );

    List<StudentEntity> findByEnrollmentEntityIsNotNull();

    List<StudentEntity> findByEnrollmentEntityIsNull();

    Page<StudentEntity> findByEnrollmentEntityIsNotNull(Pageable pageable);

    Page<StudentEntity> findByEnrollmentEntityIsNull(Pageable pageable);
}
