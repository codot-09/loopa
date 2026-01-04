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
public class AdminStatistics {

    private long usersCount;
    private long clientCount;
    private long sellerCount;
    private long productCount;
    private long requestCount;
    private List<UserResponse> topSellers;
    private double increasePercentage;
}
