package com.example.loopa.repository;

import com.example.loopa.entity.Product;
import com.example.loopa.entity.User;
import com.example.loopa.entity.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query("""
        SELECT p FROM Product p
        WHERE (:category IS NULL OR p.category = :category)
          AND p.price >= :minPrice
          AND p.price <= :maxPrice
          AND p.deleted = false
        """)
    Page<Product> search(
            @Param("category") Category category,
            @Param("minPrice") double minPrice,
            @Param("maxPrice") double maxPrice,
            Pageable pageable
    );

    List<Product> findBySeller(User seller);

    @Modifying
    @Query("UPDATE Product p SET p.totalVotes = p.totalVotes + :total, p.recommendedCount = p.recommendedCount + :recommended WHERE p.id = :id")
    void incrementVotes(@Param("id") UUID id, @Param("total") long total, @Param("recommended") long recommended);

    Page<Product> findAllByCategoryInAndDeletedFalseOrderByCreatedAtDesc(List<Category> categories, Pageable pageable);
    Page<Product> findAllByDeletedFalse(Pageable pageable);
}
