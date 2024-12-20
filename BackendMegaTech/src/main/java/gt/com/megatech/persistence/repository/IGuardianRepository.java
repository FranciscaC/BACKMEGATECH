package gt.com.megatech.persistence.repository;

import gt.com.megatech.persistence.entity.GuardianEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IGuardianRepository extends JpaRepository<GuardianEntity, Long> {

    @Query("SELECT g FROM GuardianEntity g LEFT JOIN FETCH g.studentEntities")
    List<GuardianEntity> findAllGuardiansWithStudents();

    @Query("SELECT g FROM GuardianEntity g LEFT JOIN FETCH g.studentEntities WHERE g.id = :id")
    Optional<GuardianEntity> findGuardianByIdWithStudents(@Param("id") Long id);

    boolean existsByName(String name);

    boolean existsByPhone(String phone);
}
