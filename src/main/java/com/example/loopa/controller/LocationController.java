package com.example.loopa.controller;

import com.example.loopa.annotation.CheckBlocked;
import com.example.loopa.dto.ApiResponse;
import com.example.loopa.dto.request.LocationRequest;
import com.example.loopa.dto.response.LocationResponse;
import com.example.loopa.entity.User;
import com.example.loopa.service.LocationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
@Tag(name = "Joylashuv",description = "Joylashuv malumotlarini boshqarish")
public class LocationController {

    private final LocationService locationService;

    @PostMapping("/add")
    @CheckBlocked
    public ResponseEntity<ApiResponse<String>> addLocation(
            @AuthenticationPrincipal User seller,
            @RequestBody LocationRequest request
    ){
        return ResponseEntity.ok(locationService.addLocation(seller, request));
    }

    @GetMapping("/my-locations")
    public ResponseEntity<ApiResponse<List<LocationResponse>>> getOwnLocations(@AuthenticationPrincipal User seller){
        return ResponseEntity.ok(locationService.getOwnLocations(seller));
    }

    @DeleteMapping("/{id}")
    @CheckBlocked
    public ResponseEntity<ApiResponse<String>> deleteLocation(@PathVariable UUID id){
        return ResponseEntity.ok(locationService.deleteLocation(id));
    }
}
