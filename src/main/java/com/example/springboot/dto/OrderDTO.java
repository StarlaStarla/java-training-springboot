package com.example.springboot.dto;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor // 无参构造函数
@AllArgsConstructor // 有参构造函数
@Accessors(chain = true)
@ToString
public class OrderDTO {
    private String orderId;
    private Long userId; // user 的 ID，这样避免序列化整个 User
    private String productName;
    private Integer quantity;
    private Instant expirationDate;
    private Instant createdTime;
}
