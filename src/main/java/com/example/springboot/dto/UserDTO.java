package com.example.springboot.dto;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor // 无参构造函数
@AllArgsConstructor // 有参构造函数
@ToString
public class UserDTO {
    private Long userId;
    private List<String> orderIds;
    private String userName;
    private Integer age;
    private String gender;
    private Instant createdTime;
}
