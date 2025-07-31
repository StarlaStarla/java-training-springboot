package com.example.springboot.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisSetService {

    private final RedisTemplate<String, Object> redisTemplate;

    // 向 Set 中添加元素
    public void addSet(String key, Object value) {
        redisTemplate.opsForSet().add(key, value);
    }

    // 检查元素是否存在
    public boolean isMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }
}
