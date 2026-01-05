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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionService subscriptionService;

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

        return ApiResponse.success("To'lov tasdiqlandi");
    }

    private PaymentResponse mapToResponse(Payment payment){
        return PaymentResponse.builder()
                .id(payment.getId())
                .billingUrl(payment.getBillingUrl())
                .status(payment.getStatus())
                .paymentDate(payment.getPaymentDate())
                .build();
    }
}
