package com.example.springboot.services;

import com.example.springboot.dto.OrderDTO;
import org.springframework.data.redis.core.RedisTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    // 写入数据
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set("testKey", new OrderDTO("123", 1L, "Test Product", 10, Instant.now().plus(50000, ChronoUnit.SECONDS), Instant.now()));

        byte[] bytes = redisTemplate.dump("testKey");
        System.out.println("Serialized Data: " + Arrays.toString(bytes));
    }

    public void expire(String key, long timeout){
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    // 写入数据并设置过期时间
    public void setWithExpireTime(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    // 获取数据
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // 删除数据
    public boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    // 检查是否存在
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}
