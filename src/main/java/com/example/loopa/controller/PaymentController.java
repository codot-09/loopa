package com.example.loopa.controller;

import com.example.loopa.dto.ApiResponse;
import com.example.loopa.dto.PageableRes;
import com.example.loopa.dto.response.PaymentResponse;
import com.example.loopa.entity.User;
import com.example.loopa.entity.enums.PaymentStatus;
import com.example.loopa.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/pay")
    public ResponseEntity<ApiResponse<String>> createPayment(
            @AuthenticationPrincipal User user,
            @RequestParam String billingUrl
    ){
        return ResponseEntity.ok(paymentService.createPayment(user,billingUrl));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageableRes<PaymentResponse>>> getPayments(
            @RequestParam PaymentStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page,size);
        return ResponseEntity.ok(paymentService.getPayments(status,pageable));
    }

    @PatchMapping("/approve/{id}")
    public ResponseEntity<ApiResponse<String>> approvePayment(
            @PathVariable UUID id,
            @RequestParam boolean approved
    ){
        return ResponseEntity.ok(paymentService.approvePayment(id, approved));
    }
}
