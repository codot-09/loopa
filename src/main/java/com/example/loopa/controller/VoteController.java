package com.example.loopa.controller;

import com.example.loopa.dto.ApiResponse;
import com.example.loopa.service.VoteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
@Tag(name = "Ovoz berish",description = "Ovoz berishni boshqarish")
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> vote(@RequestParam UUID productId, @RequestParam boolean recommended) {
        voteService.addVote(productId, recommended);
        return ResponseEntity.ok(ApiResponse.success("Vote added", null));
    }
}
