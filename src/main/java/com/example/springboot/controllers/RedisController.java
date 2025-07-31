package com.example.springboot.controllers;

import com.example.springboot.services.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisController {

    private final RedisService redisService;

    @GetMapping("/set")
    public String setKeyValue() {
        redisService.set("testKey", "Hello, Redis!");
        return "Key set successfully!";
    }

    @GetMapping("/get")
    public Object getKeyValue() {
        return redisService.get("userOrders::user:1:orders");
    }

    @GetMapping("/delete")
    public String deleteKey() {
        redisService.delete("testKey");
        return "Key deleted successfully!";
    }
}
