package com.example.loopa.repository;

import com.example.loopa.entity.User;
import com.example.loopa.entity.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    Page<User> findAllByRole(Role role, Pageable pageable);
}
