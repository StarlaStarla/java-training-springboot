package com.example.springboot.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisHashService {

    private final RedisTemplate<String, Object> redisTemplate;

    // 设置 Hash 键值对
    public void setHash(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    // 获取 Hash 键值
    public Object getHash(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    // 删除 Hash 键值
    public void deleteHash(String key, String field) {
        redisTemplate.opsForHash().delete(key, field);
    }

    public void setHashWithExpire(String hashKey, String field, String value, Long timeoutSeconds) {
        // 往 Hash 中写入一个字段值
        redisTemplate.opsForHash().put(hashKey, field, value);

        // 设置整个 Hash 键的过期时间
        redisTemplate.expire(hashKey, timeoutSeconds, TimeUnit.SECONDS);
    }
}
