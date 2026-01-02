package com.example.loopa.controller;

import com.example.loopa.dto.ApiResponse;
import com.example.loopa.dto.PageableRes;
import com.example.loopa.dto.request.LoginRequest;
import com.example.loopa.dto.response.LoginResponse;
import com.example.loopa.dto.response.UserResponse;
import com.example.loopa.entity.enums.Role;
import com.example.loopa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(userService.login(request));
    }

    @PatchMapping("/make-seller")
    public ResponseEntity<ApiResponse<String>> makeSeller(@RequestParam String chatId){
        return ResponseEntity.ok(userService.makeSeller(chatId));
    }

    @GetMapping("/{chatOd}")
    public ResponseEntity<ApiResponse<UserResponse>> getById(@PathVariable String chatId){
        return ResponseEntity.ok(userService.getById(chatId));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageableRes<UserResponse>>> getUsers(
            @RequestParam Role role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page,size);
        return ResponseEntity.ok(userService.getUsers(role,pageable));
    }
}
