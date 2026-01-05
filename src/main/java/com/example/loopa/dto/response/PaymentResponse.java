package com.example.loopa.dto.response;

import com.example.loopa.entity.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {

    private UUID id;
    private String billingUrl;
    private PaymentStatus status;
    private String userContact;
    private LocalDateTime paymentDate;
}
