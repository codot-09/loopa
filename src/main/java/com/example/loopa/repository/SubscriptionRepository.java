package com.example.loopa.repository;

import com.example.loopa.entity.Subscription;
import com.example.loopa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    Optional<Subscription> findBySeller(User seller);

    List<Subscription> findAllByExpireDateBefore(LocalDate date);
}
