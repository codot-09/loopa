package com.example.loopa.controller;

import com.example.loopa.dto.ApiResponse;
import com.example.loopa.dto.request.SellerRequest;
import com.example.loopa.dto.response.RequestedSellerResponse;
import com.example.loopa.entity.User;
import com.example.loopa.service.RequestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
@Tag(name = "So'rovlar",description = "Sotuvchi bo'lish uchun sor'ovlarni boshqarish")
public class RequestController {

    private final RequestService requestService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> createRequest(
            @AuthenticationPrincipal User user,
            @RequestBody SellerRequest request
    ){
        return ResponseEntity.ok(requestService.createRequest(user, request));
    }

    @PatchMapping("/make-seller/{id}")
    public ResponseEntity<ApiResponse<String>> makeSeller(@RequestParam UUID id){
        return ResponseEntity.ok(requestService.makeSeller(id));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<RequestedSellerResponse>>> getRequests(){
        return ResponseEntity.ok(requestService.getRequests());
    }
}
