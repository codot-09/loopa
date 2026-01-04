package com.example.loopa.repository;

import com.example.loopa.entity.Request;
import com.example.loopa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RequestRepository extends JpaRepository<Request, UUID> {

    boolean existsBySeller(User user);
}
