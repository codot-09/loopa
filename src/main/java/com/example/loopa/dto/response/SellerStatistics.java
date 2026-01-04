package com.example.loopa.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerStatistics {

    private long productCount;
    private List<ProductViewResponse> topProducts;
    private double competitiveness;
}
