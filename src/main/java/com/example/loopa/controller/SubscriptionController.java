package com.example.loopa.controller;

import com.example.loopa.dto.ApiResponse;
import com.example.loopa.entity.User;
import com.example.loopa.service.SubscriptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
@Tag(name = "Obunalar",description = "obunalarni boshqarish")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/info")
    public ResponseEntity<ApiResponse<LocalDate>> getInfo(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(subscriptionService.getExpireDate(user));
    }
}
