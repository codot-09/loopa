package com.example.loopa.controller;

import com.example.loopa.dto.ApiResponse;
import com.example.loopa.dto.CompetitiveAnalysis;
import com.example.loopa.dto.response.AdminStatistics;
import com.example.loopa.dto.response.SellerStatistics;
import com.example.loopa.entity.User;
import com.example.loopa.service.StatisticService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
@Tag(name = "Statistika",description = "Statistikalarni boshqarish")
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping("/admin")
    public ResponseEntity<ApiResponse<AdminStatistics>> adminStatistics(){
        return ResponseEntity.ok(statisticService.adminStatistics());
    }

    @GetMapping("/seller")
    public ResponseEntity<ApiResponse<SellerStatistics>> sellerStatistics(@AuthenticationPrincipal User seller){
        return ResponseEntity.ok(statisticService.getSellerStatistics(seller));
    }

    @GetMapping("/analysis")
    public ResponseEntity<ApiResponse<List<CompetitiveAnalysis>>> analyzeSeller(@AuthenticationPrincipal User seller){
        return ResponseEntity.ok(statisticService.getCompetitiveAnalysis(seller));
    }
}
