package com.example.springboot.controllers;

import com.example.springboot.dto.ApiResponse;
import com.example.springboot.services.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.example.springboot.constants.HttpStatusEnum.UNAUTHORIZED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private static final long SESSION_TIMEOUT = 1800; // 30分钟

    private final RedisService redisService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestParam String username, @RequestParam String password){
        // 假设验证用户名和密码成功
        if ("starla".equals(username) && "123456".equals(password)) {
            // 生成 Token，作为用户标识
            String token = UUID.randomUUID().toString();

            // 将 Token 映射到用户信息，存入 Redis，设置过期时间为 30分钟
            redisService.setWithExpireTime(token, username, SESSION_TIMEOUT);

            return ResponseEntity.ok(ApiResponse.success(token));
        }
        return ResponseEntity.ok(ApiResponse.error(UNAUTHORIZED, "用户名或密码错误！"));
    }

    @GetMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestParam String token) {
        // 删除 Redis 中存储的 Token
        redisService.delete(token);
        return ResponseEntity.ok(ApiResponse.success("注销成功！"));
    }
}
