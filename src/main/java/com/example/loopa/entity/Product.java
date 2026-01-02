package com.example.loopa.entity;

import com.example.loopa.entity.enums.Category;
import com.example.loopa.entity.enums.DeliveryType;
import com.example.loopa.entity.enums.PriceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User seller;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    private double price;

    @ManyToOne(fetch = FetchType.LAZY)
    private Location location;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private PriceType priceType;

    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType;

    private long recommendedCount;
    private long totalVotes;

    @ElementCollection
    @CollectionTable(name = "product_medias", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "media_url")
    private List<String> medias;

    private boolean deleted;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
