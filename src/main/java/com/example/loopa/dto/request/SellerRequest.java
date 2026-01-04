package com.example.loopa.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerRequest {

    private String name;
    private String sellerInfo;
    private String contact;
}
