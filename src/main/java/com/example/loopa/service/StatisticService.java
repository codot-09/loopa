package com.example.loopa.service;

import com.example.loopa.dto.ApiResponse;
import com.example.loopa.dto.response.AdminStatistics;
import com.example.loopa.dto.response.ProductViewResponse;
import com.example.loopa.dto.response.SellerStatistics;
import com.example.loopa.dto.response.UserResponse;
import com.example.loopa.entity.User;
import com.example.loopa.entity.enums.PaymentStatus;
import com.example.loopa.entity.enums.Role;
import com.example.loopa.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private final PaymentRepository paymentRepository;
    private final BannerRepository bannerRepository;

    public ApiResponse<AdminStatistics> adminStatistics() {
        long usersCount = userRepository.count();
        long sellerCount = userRepository.countByRole(Role.SELLER);
        long productCount = productRepository.count();
        long requestCount = requestRepository.count();
        long premiumUsersCount = userRepository.countByPremiumTrue();
        long bannerCount = bannerRepository.count();

        BigDecimal totalRevenue = paymentRepository.sumAmountByStatus(PaymentStatus.PAID);
        if (totalRevenue == null) totalRevenue = BigDecimal.ZERO;

        long totalPaymentsCount = paymentRepository.countByStatus(PaymentStatus.PAID);
        long pendingPaymentsCount = paymentRepository.countByStatus(PaymentStatus.PENDING);

        List<UserResponse> topSellers = productRepository.findTopSellers(PageRequest.of(0, 5))
                .stream()
                .map(userService::mapToResponse)
                .toList();

        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        long currentMonthUsers = userRepository.countByCreatedAtAfter(startOfMonth);
        long prevMonthUsers = userRepository.countByCreatedAtBetween(startOfMonth.minusMonths(1), startOfMonth);

        double increasePercentage = prevMonthUsers > 0
                ? ((double) (currentMonthUsers - prevMonthUsers) / prevMonthUsers) * 100
                : (currentMonthUsers > 0 ? 100.0 : 0.0);

        BigDecimal averageCheck = totalPaymentsCount > 0
                ? totalRevenue.divide(BigDecimal.valueOf(totalPaymentsCount), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        AdminStatistics stats = AdminStatistics.builder()
                .usersCount(usersCount)
                .clientCount(usersCount - sellerCount)
                .sellerCount(sellerCount)
                .productCount(productCount)
                .requestCount(requestCount)
                .premiumUsersCount(premiumUsersCount)
                .totalBannersCount(bannerCount)
                .totalRevenue(totalRevenue)
                .totalPaymentsCount(totalPaymentsCount)
                .pendingPaymentsCount(pendingPaymentsCount)
                .topSellers(topSellers)
                .increasePercentage(Math.round(increasePercentage * 100.0) / 100.0)
                .build();

        return ApiResponse.success(null, stats);
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
