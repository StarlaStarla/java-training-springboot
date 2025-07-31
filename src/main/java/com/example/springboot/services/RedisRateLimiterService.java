package com.example.springboot.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RedisRateLimiterService {
    private final StringRedisTemplate redisTemplate;

    private static final int MAX_REQUESTS = 3; // Limit: 3 requests per 10 seconds
    private static final long WINDOW_SECONDS = 10; // 10 seconds

    public boolean isRequestAllowed(Long key) {
        // 构建 Redis 键
        String redisKey = "rate_limit_order_for_user:" + key;
        // 当前时间戳
        long now = Instant.now().getEpochSecond();
        // 使用 Redis 的 INCR 操作递增计数
        Long currentCount = redisTemplate.opsForValue().increment(redisKey);

        if (currentCount == 1) {
            // 如果是第一次调用，设置过期时间为窗口大小
            redisTemplate.expire(redisKey, WINDOW_SECONDS, java.util.concurrent.TimeUnit.SECONDS);
        }

        // 判断是否超过限制
        return currentCount <= MAX_REQUESTS;
    }
}
