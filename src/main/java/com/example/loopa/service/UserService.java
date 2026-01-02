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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public ApiResponse<LoginResponse> login(LoginRequest request) {
        Optional<User> optionalUser = userRepository.findById(request.getChatId());
        boolean isNewUser = optionalUser.isEmpty();

        User user = optionalUser.orElseGet(() -> createUser(request));

        String token = jwtProvider.generateToken(user.getChatId());

        LoginResponse response = LoginResponse.builder()
                .token(token)
                .role(user.getRole().name())
                .newUser(isNewUser)
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
        userRepository.save(user);

        return ApiResponse.success("Hammasi tayyor", null);
    }

    public ApiResponse<String> makeSeller(String chatId) {
        User user = userRepository.findById(chatId)
                .orElseThrow(() -> new DataNotFoundException("Foydalanuvchi topilmadi"));

        user.setRole(Role.SELLER);
        userRepository.save(user);

        return ApiResponse.success("Foydalanuvchi endi sotuvchi");
    }

    public ApiResponse<UserResponse> getById(String chatId) {
        return userRepository.findById(chatId)
                .map(user -> ApiResponse.success((String) null, mapToResponse(user)))
                .orElseThrow(() -> new DataNotFoundException("Foydalanuvchi topilmadi"));
    }

    public ApiResponse<PageableRes<UserResponse>> getUsers(Role role, Pageable pageable) {
        Page<UserResponse> userResponsePage = userRepository.findAllByRole(role, pageable)
                .map(this::mapToResponse);

        return ApiResponse.success(null, PageableRes.fromPage(userResponsePage));
    }

    private User createUser(LoginRequest request) {
        User user = User.builder()
                .chatId(request.getChatId())
                .username(request.getUsername())
                .role(Role.CLIENT)
                .isBlocked(false)
                .build();
        return userRepository.save(user);
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getChatId(),
                user.getUsername(),
                user.getRole().name()
        );
    }
}
