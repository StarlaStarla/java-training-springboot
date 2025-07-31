package com.example.springboot.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@NoArgsConstructor // 无参构造函数
@AllArgsConstructor // 有参构造函数
@Entity
@Getter
@Setter
@Table(name = "users")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long userId;

    @OneToMany(mappedBy="user", orphanRemoval = true)
//    @JsonManagedReference // 此字段为主控方，允许序列化
    private List<Order> orders;

    @NotBlank(message = "User name cannot be blank")
    @Column(name = "user_name", nullable = false)
    private String userName;

    private Integer age;
    private String gender;

    @Column(name = "created_at", updatable = false)
    private Instant createdTime;
}
