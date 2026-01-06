package com.example.loopa.repository;

import com.example.loopa.entity.User;
import com.example.loopa.entity.enums.Category;
import com.example.loopa.entity.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    Page<User> findAllByRole(Role role, Pageable pageable);

    @Query("""
        SELECT c FROM User u
        JOIN u.favouriteCategories c
        WHERE u.chatId = :userId
    """)
    List<Category> findFavouriteCategories(@Param("userId") String userId);

    long countByRole(Role role);

    long countByCreatedAtAfter(LocalDateTime dateTime);

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<User> findAllByLastLoginBefore(LocalDateTime date);

    long countByPremiumTrue();

}
