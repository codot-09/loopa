package com.example.loopa.repository;

import com.example.loopa.entity.Payment;
import com.example.loopa.entity.User;
import com.example.loopa.entity.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Page<Payment> findAllByStatus(PaymentStatus status, Pageable pageable);

    List<Payment> findByUser(User user);

    long countByStatus(PaymentStatus status);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = :status")
    BigDecimal sumAmountByStatus(@Param("status") PaymentStatus status);

    // Agar tushumni vaqt bo'yicha filter qilmoqchi bo'lsangiz
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = :status AND p.paymentDate > :after")
    BigDecimal sumAmountByStatusAndDateAfter(@Param("status") PaymentStatus status, @Param("after") LocalDateTime after);
}
