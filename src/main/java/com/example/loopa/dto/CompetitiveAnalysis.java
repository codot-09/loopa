package com.example.loopa.dto;

import com.example.loopa.dto.response.ProductViewResponse;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class CompetitiveAnalysis {
    private String categoryName;
    private long totalCompetitors;
    private long totalProductsInCategory;
    private double averagePrice;
    private double minPrice;
    private double maxPrice;
    private List<ProductViewResponse> topViewedProducts;
    private double marketShare;
}
