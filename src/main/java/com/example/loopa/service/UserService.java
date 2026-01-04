package com.example.loopa.service;

import com.example.loopa.dto.ApiResponse;
import com.example.loopa.dto.PageableRes;
import com.example.loopa.dto.request.LoginRequest;
import com.example.loopa.dto.request.SellerRequest;
import com.example.loopa.dto.response.LoginResponse;
import com.example.loopa.dto.response.UserResponse;
import com.example.loopa.entity.User;
import com.example.loopa.entity.enums.Category;
import com.example.loopa.entity.enums.Role;
import com.example.loopa.exception.DataNotFoundException;
import com.example.loopa.repository.UserRepository;
import com.example.loopa.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public ApiResponse<LoginResponse> login(LoginRequest request) {
        User user = userRepository.findById(request.getChatId())
                .orElseGet(() -> createUser(request));

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtProvider.generateToken(user.getChatId());

        LoginResponse response = LoginResponse.builder()
                .token(token)
                .role(user.getRole().name())
                .newUser(user.isNewUser())
                .build();

        return ApiResponse.success(null, response);
    }

    public ApiResponse<LoginResponse> adminLogin(String id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Foydalanuvchi topilmadi"));

        String token = jwtProvider.generateToken(user.getChatId());

        LoginResponse response = LoginResponse.builder()
                .token(token)
                .role(user.getRole().name())
                .newUser(false)
                .build();

        return ApiResponse.success(null, response);
    }

    @Transactional
    public ApiResponse<String> setInterests(User user, List<String> interests) {
        List<Category> categories = interests.stream()
                .map(interest -> {
                    try {
                        return Category.valueOf(interest.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new DataNotFoundException("Kategoriya topilmadi: " + interest);
                    }
                })
                .toList();

        user.setFavouriteCategories(categories);
        user.setNewUser(false);
        userRepository.save(user);

        return ApiResponse.success("Hammasi tayyor", null);
    }

    public ApiResponse<UserResponse> getById(String chatId) {
        User user = userRepository.findById(chatId)
                .orElseThrow(() -> new DataNotFoundException("Foydalanuvchi topilmadi"));

        return ApiResponse.success(null, mapToResponse(user));
    }

    public ApiResponse<PageableRes<UserResponse>> getUsers(Role role, Pageable pageable) {
        Page<UserResponse> userResponsePage = userRepository.findAllByRole(role, pageable)
                .map(this::mapToResponse);

        return ApiResponse.success(null, PageableRes.fromPage(userResponsePage));
    }

    private User createUser(LoginRequest request) {
        return userRepository.save(User.builder()
                .chatId(request.getChatId())
                .tgUsername(request.getUsername())
                .role(Role.CLIENT)
                .newUser(true)
                .isBlocked(false)
                .build());
    }

    public UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getChatId(),
                user.getUsername(),
                user.getRole().name()
        );
    }
}
