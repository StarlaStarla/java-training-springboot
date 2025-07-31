package com.example.springboot.controllers;

import com.example.springboot.constants.HttpStatusEnum;
import com.example.springboot.dto.ApiResponse;
import com.example.springboot.dto.OrderDTO;
import com.example.springboot.entities.Order;
import com.example.springboot.services.OrderService;
import com.example.springboot.services.RedisRateLimiterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final RedisRateLimiterService redisRateLimiterService;

    @GetMapping("/user/{userId}")
    public ResponseEntity queryOrders(@PathVariable Long userId) {
        // 配置：每分钟最多允许 5 次请求
        boolean allowed = redisRateLimiterService.isRequestAllowed(userId);
        if (allowed) {
            try {
                return ResponseEntity.ok(orderService.queryOrders(userId));
            } catch (Exception e) {
                System.out.println("error------------"+ e.getMessage());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too Many Requests");
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity queryOrderById(@PathVariable String orderId) {
        try {
            return ResponseEntity.ok(orderService.queryOrderById(orderId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity deleteOrder(@PathVariable String orderId) {
        try {
            orderService.deleteOrder(orderId);
            return ResponseEntity.ok("Delete order success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @PostMapping()
    public ResponseEntity addOrder(@Valid @RequestBody OrderDTO order) {
        orderService.addOrder(order);
        return ResponseEntity.ok("Add order success");
    }

    @PutMapping("/{orderId}")
    public ResponseEntity updateOrder(@PathVariable String orderId, @RequestBody OrderDTO order) {
        try {
            orderService.updateOrder(orderId, order);
            return ResponseEntity.ok("Update order success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
