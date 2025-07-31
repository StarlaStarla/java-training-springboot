package com.example.springboot.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HttpStatusEnum {
    SUCCESS(200, "OK"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    NOT_FOUND(404, "Not Found"),
    Product_NOT_FOUND(405, "Product Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    TOO_MANY_REQUESTS(429, "Too Many Requests");


    private final int code;
    private final String message;
}
