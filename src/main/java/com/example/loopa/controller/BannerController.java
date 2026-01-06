package com.example.loopa.controller;

import com.example.loopa.dto.ApiResponse;
import com.example.loopa.dto.BannerDTO;
import com.example.loopa.entity.User;
import com.example.loopa.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> addBanner(@AuthenticationPrincipal User user, @RequestBody BannerDTO request) {
        return ResponseEntity.ok(bannerService.addBanner(user,request));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BannerDTO>>> getBanners() {
        return ResponseEntity.ok(bannerService.getBanners());
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<BannerDTO>> getOwnBanner(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(bannerService.getOwnBanner(user));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> deleteBanner(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(bannerService.deleteBanner(user));
    }
}
