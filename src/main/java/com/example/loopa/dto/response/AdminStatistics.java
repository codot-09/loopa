package com.example.loopa.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminStatistics {

    private long usersCount;
    private long clientCount;
    private long sellerCount;
    private long productCount;
    private long requestCount; // Yangi seller so'rovlari
    private List<UserResponse> topSellers;
    private double increasePercentage; // Foydalanuvchilar o'sish foizi

    // --- Moliyaviy fieldlar (Revenue & Payments) ---
    private BigDecimal totalRevenue;       // Umumiy tushum (summa)
    private BigDecimal monthlyRevenue;     // Oxirgi 30 kunlik tushum
    private long totalPaymentsCount;       // Jami muvaffaqiyatli to'lovlar soni
    private long pendingPaymentsCount;     // Tasdiqlanishi kutilayotgan to'lovlar

    // --- Foydalanuvchilar segmentatsiyasi ---
    private long premiumUsersCount;        // Premium foydalanuvchilar soni
    private long activeUsersLast24h;       // Oxirgi 24 soatda botdan foydalanganlar

    // --- Kontent va Marketing ---
    private long totalBannersCount;        // Faol bannerlar soni
    private long totalCategoriesCount;     // Kategoriyalar soni

    private double conversionRate;
}
