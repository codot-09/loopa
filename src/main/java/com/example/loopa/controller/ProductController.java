package com.example.loopa.controller;

import com.example.loopa.dto.ApiResponse;
import com.example.loopa.dto.PageableRes;
import com.example.loopa.dto.request.ProductCreateRequest;
import com.example.loopa.dto.response.ProductViewResponse;
import com.example.loopa.entity.User;
import com.example.loopa.entity.enums.Category;
import com.example.loopa.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/new-product")
    public ResponseEntity<ApiResponse<String>> createProduct(
            @AuthenticationPrincipal User seller,
            @RequestBody ProductCreateRequest request
    ){
        return ResponseEntity.ok(productService.createProduct(seller, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable UUID id){
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageableRes<ProductViewResponse>>> searchProducts(
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page,size);
        return ResponseEntity.ok(productService.searchProducts(category,minPrice,maxPrice,pageable));
    }

    @GetMapping("/for-user")
    public ResponseEntity<ApiResponse<PageableRes<ProductViewResponse>>> getForUser(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ){
        Pageable pageable = PageRequest.of(page,size);
        return ResponseEntity.ok(productService.getForUser(user,pageable));
    }
}
