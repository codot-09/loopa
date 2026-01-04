package com.example.loopa.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestedSellerResponse {

    private UUID requestId;
    private String name;
    private String sellerInfo;
    private String contact;
}
