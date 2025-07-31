package com.example.springboot.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {

    /**
     * 配置全局的 ObjectMapper，用于 Jackson 序列化和反序列化,数据的序列化（Java对象转 JSON） 和 反序列化（JSON 转 Java对象）
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // 1. 注册 Java 8 日期和时间模块（`JavaTimeModule` 用于序列化和反序列化 `LocalDateTime` 等）
        objectMapper.registerModule(new JavaTimeModule());
        // 避免序列化时写入时间戳
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 2. 指定序列化时包含的字段（只包含非 null 字段）
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        objectMapper.findAndRegisterModules();

        // 3. 配置 JSON 模式：忽略未知属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper;
    }

    /**
     * 配置 RedisTemplate，设置 Key 和 Value 的序列化方式
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 配置 Key 的序列化方式为 String
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        // 使用 JSON 序列化 Value，使用自定义 ObjectMapper，支持泛型
        ObjectMapper objectMapper = objectMapper();
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 配置 RedisCacheManager，支持注解 @Cacheable、@CacheEvict 等
     */
    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory,
                                               RedisTemplate<String, Object> redisTemplate) {
        // 提取 RedisTemplate 的序列化配置
        StringRedisSerializer stringSerializer = (StringRedisSerializer) redisTemplate.getKeySerializer();
        GenericJackson2JsonRedisSerializer valueSerializer =
                (GenericJackson2JsonRedisSerializer) redisTemplate.getValueSerializer();

        // 默认缓存配置（适用于全局通用缓存）
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))  // 默认 TTL：10分钟
                .disableCachingNullValues()        // 禁用 null 值缓存
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer));

        // 特定缓存配置（为 "users" 缓存设置 TTL）
        RedisCacheConfiguration ordersCacheConfig = defaultConfig
                .entryTtl(Duration.ofHours(1)); // 单独设置 TTL 为 1小时

        // 构建 RedisCacheManager
        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(defaultConfig)                     // 默认配置
                .withCacheConfiguration("userOrders", ordersCacheConfig) // 应用 "orders" 配置
                .build();
    }
}
