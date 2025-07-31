package com.example.springboot.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisListService {

    private final RedisTemplate<String, Object> redisTemplate;

    // 从左侧插入
    public void pushLeft(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    // 从右侧插入
    public void pushRight(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    // 取出左侧元素
    public Object popLeft(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    // 取出右侧元素
    public Object popRight(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }
}