package com.example.loopa.repository;

import com.example.loopa.entity.Location;
import com.example.loopa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {

    List<Location> findBySeller(User seller);
}
