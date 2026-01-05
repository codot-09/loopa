package com.example.loopa.service;

import com.example.loopa.dto.ApiResponse;
import com.example.loopa.entity.Subscription;
import com.example.loopa.entity.User;
import com.example.loopa.exception.DataNotFoundException;
import com.example.loopa.repository.SubscriptionRepository;
import com.example.loopa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    public void updateSubscription(User user){
        Subscription subscription = subscriptionRepository.findBySeller(user)
                .orElseGet(() -> createSubscription(user));

        subscription.setExpireDate(LocalDate.now().plusMonths(1));
        subscriptionRepository.save(subscription);
    }

    public ApiResponse<LocalDate> getExpireDate(User user){
        Subscription subscription = subscriptionRepository.findBySeller(user)
                .orElseThrow(() -> new DataNotFoundException("Obuna topilmadi"));

        return ApiResponse.success(null,subscription.getExpireDate());
    }

    @Transactional
    public Subscription createSubscription(User user){

        user.setPremium(true);
        userRepository.save(user);

        return subscriptionRepository.save(Subscription.builder()
                .seller(user)
                .build());
    }
}
