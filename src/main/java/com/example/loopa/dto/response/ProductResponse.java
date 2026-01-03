package com.example.loopa.dto.response;

import com.example.loopa.entity.enums.Category;
import com.example.loopa.entity.enums.DeliveryType;
import com.example.loopa.entity.enums.PriceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {

    private String name;
    private String description;
    private double price;
    private PriceType priceType;
    private DeliveryType deliveryType;
    private Category category;
    private LocationResponse locationResponse;
    private String sellerContact;
    private List<String> medias;
    private LocalDateTime createdAt;
    private double recommendedPrecent;
}
