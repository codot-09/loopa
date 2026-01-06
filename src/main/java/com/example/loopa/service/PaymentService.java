package com.example.loopa.service;

import com.example.loopa.dto.ApiResponse;
import com.example.loopa.dto.PageableRes;
import com.example.loopa.dto.response.PaymentResponse;
import com.example.loopa.entity.Payment;
import com.example.loopa.entity.User;
import com.example.loopa.entity.enums.PaymentStatus;
import com.example.loopa.exception.DataNotFoundException;
import com.example.loopa.repository.PaymentRepository;
import com.example.loopa.repository.SubscriptionRepository;
import com.example.loopa.repository.UserRepository;
import com.example.loopa.service.bot.BotService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionService subscriptionService;
    private final UserRepository userRepository;
    private final BotService botService;

    public ApiResponse<String> createPayment(User user,String billingUrl){
        Payment payment = Payment.builder()
                .user(user)
                .billingUrl(billingUrl)
                .status(PaymentStatus.PENDING)
                .build();

        paymentRepository.save(payment);

        return ApiResponse.success("Tasdiqlash kutilmoqda");
    }

    public ApiResponse<PageableRes<PaymentResponse>> getPayments(PaymentStatus status, Pageable pageable) {
        Page<PaymentResponse> payments = paymentRepository.findAllByStatus(status, pageable).map(this::mapToResponse);
        return ApiResponse.success(null, PageableRes.fromPage(payments));
    }

    @Transactional
    public ApiResponse<String> approvePayment(UUID paymentId,boolean approved) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new DataNotFoundException("To'lov topilmadi"));

        payment.setStatus(approved ? PaymentStatus.PAID : PaymentStatus.FAILED);
        paymentRepository.save(payment);

        if (approved){
            subscriptionService.updateSubscription(payment.getUser());
        }

        String message = "✨ **Tabriklaymiz, PRO-status faollashtirildi!**\n\n" +
                "Hurmatli sotuvchi, Loopa platformasining barcha professional imkoniyatlaridan foydalanish huquqiga ega bo'ldingiz. Endi mahsulotlaringiz qidiruv natijalarida yuqori o'rinlarda ko'rinadi va sizga maxsus **PRO** badge berildi.\n\n" +
                "🚀 **Yangi imkoniyatlaringiz:**\n" +
                "• Bozor tahlili va raqobatchilar analitikasi\n" +
                "• Reklama bannerlarini joylashtirish huquqi\n" +
                "• Mijozlar ishonchi va yuqori konversiya\n\n" +
                "Bizni tanlaganingiz uchun tashakkur! Savdolaringiz barakali bo'lishini tilaymiz.";

        botService.sendMessage(Long.valueOf(payment.getUser().getChatId()), message);

        return ApiResponse.success("To'lov tasdiqlandi");
    }

    public ApiResponse<List<PaymentResponse>> getByUser(String id){

        User user = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Foydalanuvchi topilmadi"));

        List<PaymentResponse> paymentResponses = paymentRepository.findByUser(user).stream().map(this::mapToResponse).toList();
        return ApiResponse.success(null,paymentResponses);
    }

    private PaymentResponse mapToResponse(Payment payment){
        return PaymentResponse.builder()
                .id(payment.getId())
                .billingUrl(payment.getBillingUrl())
                .status(payment.getStatus())
                .userContact(payment.getUser().getPhone())
                .paymentDate(payment.getPaymentDate())
                .build();
    }
}
