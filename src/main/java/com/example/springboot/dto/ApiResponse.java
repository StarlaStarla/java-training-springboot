package com.example.springboot.dto;

import com.example.springboot.constants.HttpStatusEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> {
    private Integer statusCode;
    private String message;
    private T body;

    // 成功响应：有数据
    public static <T> ApiResponse<T> success(T body) {
        return ApiResponse.<T>builder()
                .statusCode(HttpStatusEnum.SUCCESS.getCode())
                .message(HttpStatusEnum.SUCCESS.getMessage())
                .body(body)
                .build();
    }

    // 成功响应：无数据
    public static <T> ApiResponse<T> success() {
        return ApiResponse.<T>builder()
                .statusCode(HttpStatusEnum.SUCCESS.getCode())
                .message(HttpStatusEnum.SUCCESS.getMessage())
                .build();
    }

    // 错误响应
    public static <T> ApiResponse<T> error(HttpStatusEnum status, String customMessage) {
        return ApiResponse.<T>builder()
                .statusCode(status.getCode())
                .message(customMessage != null ? customMessage : status.getMessage())
                .body(null)
                .build();
    }
}
