package com.example.loopa.service;

import com.example.loopa.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, String> hashOps;
    private final ProductRepository productRepository;

    public void addVote(UUID productId, boolean recommended) {
        String key = "product:votes:" + productId;
        hashOps.increment(key, "totalVotes", 1);
        if (recommended) {
            hashOps.increment(key, "recommendedVotes", 1);
        }
        redisTemplate.expire(key, Duration.ofDays(1));
    }

    @Scheduled(fixedRate = 10000)
    @Transactional
    public void flushVotesToDb() {
        Set<String> keys = redisTemplate.keys("product:votes:*");
        if (keys == null || keys.isEmpty()) return;

        for (String key : keys) {
            UUID productId = UUID.fromString(key.split(":")[2]);
            Map<String, String> votes = hashOps.entries(key);

            long total = Long.parseLong(votes.getOrDefault("totalVotes", "0"));
            long recommended = Long.parseLong(votes.getOrDefault("recommendedVotes", "0"));

            productRepository.incrementVotes(productId, total, recommended);
            redisTemplate.delete(key);
        }
    }
}
