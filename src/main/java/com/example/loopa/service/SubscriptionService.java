package com.example.loopa.service;

import com.example.loopa.dto.ApiResponse;
import com.example.loopa.entity.Subscription;
import com.example.loopa.entity.User;
import com.example.loopa.exception.DataNotFoundException;
import com.example.loopa.repository.SubscriptionRepository;
import com.example.loopa.repository.UserRepository;
import com.example.loopa.service.bot.BotService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final BotService botService;

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

    @Scheduled(cron = "0 0 0 * * *")
    public void inactiveSubscriptions() {
        LocalDate now = LocalDate.now();
        List<Subscription> expiredSubscriptions = subscriptionRepository
                .findAllByExpireDateBefore(now);

        for (Subscription subscription : expiredSubscriptions) {
            User user = subscription.getSeller();
            user.setPremium(false);
            userRepository.save(user);

            String message = "⚠️ **PRO-status muddati tugadi**\n\n" +
                    "Hurmatli sotuvchi, obuna muddati yakunlanganligi sababli PRO imkoniyatlar to'xtatildi. " +
                    "Sotuv hajmini saqlab qolish uchun obunani yangilashingizni tavsiya qilamiz.";

            botService.sendMessage(Long.valueOf(user.getChatId()), message);
        }
    }
}
