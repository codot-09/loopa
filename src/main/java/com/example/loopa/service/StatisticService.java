package com.example.loopa.service;

import com.example.loopa.dto.ApiResponse;
import com.example.loopa.dto.response.AdminStatistics;
import com.example.loopa.dto.response.ProductViewResponse;
import com.example.loopa.dto.response.SellerStatistics;
import com.example.loopa.dto.response.UserResponse;
import com.example.loopa.entity.User;
import com.example.loopa.entity.enums.Role;
import com.example.loopa.repository.ProductRepository;
import com.example.loopa.repository.RequestRepository;
import com.example.loopa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final RequestRepository requestRepository;
    private final UserService userService;
    private final ProductService productService;

    public ApiResponse<AdminStatistics> adminStatistics() {
        long usersCount = userRepository.count();
        long sellerCount = userRepository.countByRole(Role.SELLER);
        long clientCount = usersCount - sellerCount;
        long productCount = productRepository.count();
        long requestCount = requestRepository.count();

        List<UserResponse> topSellers = productRepository.findTopSellers(PageRequest.of(0, 5))
                .stream()
                .map(userService::mapToResponse)
                .toList();

        LocalDateTime startOfCurrentMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        long currentMonthUsers = userRepository.countByCreatedAtAfter(startOfCurrentMonth);
        long previousMonthUsers = userRepository.countByCreatedAtBetween(
                startOfCurrentMonth.minusMonths(1),
                startOfCurrentMonth
        );

        double increasePercentage = 0.0;
        if (previousMonthUsers > 0) {
            increasePercentage = ((double) (currentMonthUsers - previousMonthUsers) / previousMonthUsers) * 100;
        } else if (currentMonthUsers > 0) {
            increasePercentage = 100.0;
        }

        AdminStatistics stats = AdminStatistics.builder()
                .usersCount(usersCount)
                .clientCount(clientCount)
                .sellerCount(sellerCount)
                .productCount(productCount)
                .requestCount(requestCount)
                .topSellers(topSellers)
                .increasePercentage(Math.round(increasePercentage * 100.0) / 100.0)
                .build();

        return ApiResponse.success(null,stats);
    }

    public ApiResponse<SellerStatistics> getSellerStatistics(User currentUser) {

        long productCount = productRepository.countBySellerChatId(currentUser.getChatId());

        List<ProductViewResponse> topProducts = productRepository.findTopProductsBySeller(
                        currentUser.getChatId(),
                        PageRequest.of(0, 5)
                ).stream()
                .map(productService::mapToViewResponse)
                .toList();

        long totalMarketProducts = productRepository.countByCategoryIn(
                productRepository.findCategoriesBySellerId(currentUser.getChatId())
        );

        double competitiveness = 0.0;
        if (totalMarketProducts > 0) {
            competitiveness = ((double) productCount / totalMarketProducts) * 100;
        }

        SellerStatistics stats = SellerStatistics.builder()
                .productCount(productCount)
                .topProducts(topProducts)
                .competitiveness(Math.round(competitiveness * 10.0) / 10.0)
                .build();

        return ApiResponse.success(null,stats);
    }
}
