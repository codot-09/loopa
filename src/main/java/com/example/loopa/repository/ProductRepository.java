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

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query("""
        SELECT p FROM Product p
        WHERE p.deleted = false
          AND (:query IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')))
          AND (:category IS NULL OR p.category = :category)
          AND (:minPrice IS NULL OR p.price >= :minPrice)
          AND (:maxPrice IS NULL OR p.price <= :maxPrice)
        """)
    Page<Product> search(
            @Param("query") String query,
            @Param("category") Category category,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable
    );

    List<Product> findBySeller(User seller);

    @Modifying
    @Query("UPDATE Product p SET p.totalVotes = p.totalVotes + :total, p.recommendedCount = p.recommendedCount + :recommended WHERE p.id = :id")
    void incrementVotes(@Param("id") UUID id, @Param("total") long total, @Param("recommended") long recommended);

    Page<Product> findAllByCategoryInAndDeletedFalseOrderByCreatedAtDesc(List<Category> categories, Pageable pageable);
    Page<Product> findAllByDeletedFalse(Pageable pageable);

    long countBySellerChatId(String sellerId);

    @Query("SELECT p FROM Product p WHERE p.seller.chatId = :sellerId ORDER BY p.recommendedCount DESC")
    List<Product> findTopProductsBySeller(@Param("sellerId") String sellerId, Pageable pageable);

    @Query("SELECT DISTINCT p.category FROM Product p WHERE p.seller.chatId = :sellerId")
    List<Category> findCategoriesBySellerId(@Param("sellerId") String sellerId);

    long countByCategoryIn(List<Category> categories);

    @Query("SELECT p.seller FROM Product p WHERE p.seller.role = 'SELLER' GROUP BY p.seller ORDER BY COUNT(p) DESC")
    List<User> findTopSellers(Pageable pageable);

    Page<Product> findAllBySeller_PremiumTrue(Pageable pageable);

    @Query("SELECT p.price FROM Product p WHERE p.category = :category")
    List<Double> findAllPricesByCategory(@Param("category") Category category);

    long countBySellerAndCategory(User seller, Category category);

    @Query("SELECT COUNT(DISTINCT p.seller) FROM Product p WHERE p.category = :category")
    long countSellersByCategory(@Param("category") Category category);

    List<Product> findTop5ByCategoryOrderByRecommendedCountDesc(Category category);
}
