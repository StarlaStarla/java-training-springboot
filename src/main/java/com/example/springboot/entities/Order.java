package com.example.springboot.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;

import java.time.Instant;

@NoArgsConstructor // 无参构造函数
@AllArgsConstructor // 有参构造函数
@Entity
@Getter
@Setter
@Table(name = "orders")
@DynamicInsert  // 仅插入非空字段, 让 Hibernate 在插入数据时仅包含非空字段，而忽略未赋值的字段，因此 created_at 将不被显式插入，数据库会应用默认值 CURRENT_TIMESTAMP。
@Accessors(chain = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "orderId")
public class Order {

    @Id
    @Column(name = "id")
    private String orderId;

    @NotBlank(message = "Product name cannot be blank")
    @Column(name = "product_name", nullable = false)
    private String productName;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @Future(message = "Expiry date should be in the future") // 验证过期时间必须是未来时间
    @Column(name = "expiration_date", nullable = false)
    private Instant expirationDate;

//    @CreationTimestamp  // Hibernate 自动填充当前时间
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdTime;

    @NotNull(message = "user_id cannot be null")  // 验证 user_id 不为空
    @Column(name = "user_id")  // 与 user_id 数据库列直接对应
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
//    @JsonBackReference // 此字段不会被序列化，避免双向递归
    private User user;

}