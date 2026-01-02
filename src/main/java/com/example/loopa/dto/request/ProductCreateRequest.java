package com.example.loopa.dto.request;

import com.example.loopa.entity.enums.Category;
import com.example.loopa.entity.enums.DeliveryType;
import com.example.loopa.entity.enums.PriceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateRequest {

    private String name;
    private String description;
    private double price;
    private List<String> medias;
    private Category category;
    private PriceType priceType;
    private DeliveryType deliveryType;
    private UUID locationId;
}
