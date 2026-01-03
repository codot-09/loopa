package com.example.loopa.controller;

import com.example.loopa.dto.ApiResponse;
import com.example.loopa.dto.response.ProductViewResponse;
import com.example.loopa.entity.User;
import com.example.loopa.service.FavouriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/favourite")
@RequiredArgsConstructor
public class FavouriteController {

    private final FavouriteService favouriteService;

    @PostMapping("/{productId}")
    public ResponseEntity<ApiResponse<String>> add(@AuthenticationPrincipal User user, @PathVariable UUID productId){
        return ResponseEntity.ok(favouriteService.addFavourites(user, productId));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<ProductViewResponse>>> getFavourites(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(favouriteService.getFavourites(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteFavourites(@PathVariable UUID id){
        return ResponseEntity.ok(favouriteService.deleteFavourites(id));
    }
}
