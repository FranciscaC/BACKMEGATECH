package gt.com.megatech.persistence.repository;

import gt.com.megatech.persistence.entity.PaymentEntity;
import gt.com.megatech.persistence.entity.StudentEntity;
import gt.com.megatech.persistence.entity.enums.MonthEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPaymentRepository extends JpaRepository<PaymentEntity, Long> {

    boolean existsByStudentEntityAndMonthEnumAndYear(
            StudentEntity studentEntity,
            MonthEnum monthEnum,
            Integer year
    );

    List<PaymentEntity> findByMonthEnumAndYear(
            MonthEnum monthEnum,
            Integer year
    );

    Page<PaymentEntity> findByMonthEnumAndYear(
            MonthEnum monthEnum,
            Integer year,
            Pageable pageable
    );

    List<PaymentEntity> findByStudentEntity_Id(Long studentId);
}
