package com.example.loopa.repository;

import com.example.loopa.entity.Banner;
import com.example.loopa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BannerRepository extends JpaRepository<Banner, UUID> {

    boolean existsBySeller(User user);
    Optional<Banner> findBySeller(User seller);
}
