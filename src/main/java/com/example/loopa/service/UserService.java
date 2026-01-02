package com.example.loopa.service;

import com.example.loopa.dto.ApiResponse;
import com.example.loopa.dto.PageableRes;
import com.example.loopa.dto.request.LoginRequest;
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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public ApiResponse<LoginResponse> login(LoginRequest request) {
        User user = userRepository.findById(request.getChatId())
                .orElseGet(() -> createUser(request));

        String token = jwtProvider.generateToken(user.getChatId());

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setRole(user.getRole().name());
        response.setNewUser(user.isNewUser());

        return ApiResponse.success(null, response);
    }

    public ApiResponse<String> setInterests(User user, List<String> interests) {
        List<Category> categories = interests.stream()
                .map(interest -> {
                    try {
                        return Category.valueOf(interest.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException("Kategoriya topilmadi: " + interest);
                    }
                })
                .toList();

        user.setFavouriteCategories(categories);
        user.setNewUser(false);
        userRepository.save(user);

        return ApiResponse.success("Hammasi tayyor", null);
    }

    public ApiResponse<String> makeSeller(String chatId){
        User user = userRepository.findById(chatId)
                .orElseThrow(() -> new DataNotFoundException("Foydalanuvchi topilmadi"));

        user.setRole(Role.SELLER);
        userRepository.save(user);

        return ApiResponse.success("O'zgarishlar saqlandi");
    }

    public ApiResponse<UserResponse> getById(String chatId){
        User user = userRepository.findById(chatId)
                .orElseThrow(() -> new DataNotFoundException("Foydalanuvchi topilmadi"));

        return ApiResponse.success(null,mapToResponse(user));
    }

    public ApiResponse<PageableRes<UserResponse>> getUsers(Role role, Pageable pageable) {
        Page<UserResponse> userResponsePage = userRepository.findAllByRole(role, pageable)
                .map(this::mapToResponse);

        return ApiResponse.success(null, PageableRes.fromPage(userResponsePage));
    }

    private User createUser(LoginRequest request){

        return userRepository.save(User.builder()
                .chatId(request.getChatId())
                .username(request.getUsername())
                .role(Role.CLIENT)
                .isBlocked(false)
                .build());
    }

    private UserResponse mapToResponse(User user){
        return new UserResponse(
                user.getChatId(),
                user.getUsername(),
                user.getRole().name()
        );
    }
}
