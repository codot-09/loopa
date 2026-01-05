package com.example.loopa.repository;

import com.example.loopa.entity.Payment;
import com.example.loopa.entity.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Page<Payment> findAllByStatus(PaymentStatus status, Pageable pageable);
}
