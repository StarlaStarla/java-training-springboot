package com.example.springboot.services;

import com.example.springboot.dto.OrderDTO;
import com.example.springboot.entities.Order;
import com.example.springboot.mappers.OrderMapper;
import com.example.springboot.repositories.OrderRepository;
import com.example.springboot.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.springboot.specifications.OrderSpecification;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    private final OrderSpecification orderSpecification;
    private final OrderMapper orderMapper;

    @Cacheable(value = "userOrders", key = "'user:' + #userId + ':orders'")
    public List<OrderDTO> queryOrders(Long userId) {
        System.out.println("查询数据库...缓存生效不会再查询数据库");
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found for id:" + userId));
        Specification<Order> spec = orderSpecification.hasUserId(userId);
        List<Order> orders = orderRepository.findAll(spec);
        List<OrderDTO> orderDTOS =  orderMapper.toOrderDTOList(orders);
        orderDTOS = orderDTOS.stream().sorted(Comparator.comparing(OrderDTO::getCreatedTime)).toList();
        return orderDTOS;
    }

    @Cacheable(value = "order", key = "#orderId")
    public OrderDTO queryOrderById(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found for id:" + orderId));
        return orderMapper.toDTO(order);
    }

    @CachePut(value = "userOrders", key = "'user:' + #orderDTO.userId + ':orders'")
    public void addOrder(OrderDTO orderDTO) {
//        if (order.getExpirationDate().isBefore(LocalDateTime.now())) {
//            return ApiResponse.error(HttpStatusEnum.BAD_REQUEST, "Failed to add order. Product is expired.");
//        }
        orderDTO.setOrderId(UUID.randomUUID().toString());
        orderDTO.setExpirationDate(orderDTO.getExpirationDate().atZone(ZoneId.systemDefault()).toInstant());
        Order order = orderMapper.toEntity(orderDTO);
        orderRepository.save(order);
    }

    // 删除一个 Order
    @CacheEvict(value = "order", key = "#orderId")
    public void deleteOrder(String orderId) {
        orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found for id:" + orderId));
        orderRepository.deleteById(orderId);
    }

    // 修改一个 Order
    @CachePut(value = "userOrders", key = "'user:' + #updatedOrder.userId + ':orders'")
    public OrderDTO updateOrder(String orderId, OrderDTO updatedOrder) {
        Order existingOrder = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found for id:" + orderId));
        if (existingOrder.getExpirationDate().isBefore(Instant.now())) {
            throw new IllegalStateException("Failed to update order. Product is expired.");
        }

        if (updatedOrder.getQuantity() < 1) {
            throw new IllegalStateException("Failed to update order. Quantity must be at least 1.");
        }

        existingOrder.setProductName(updatedOrder.getProductName());
        existingOrder.setQuantity(updatedOrder.getQuantity());
        existingOrder.setExpirationDate(updatedOrder.getExpirationDate());
        // 保存更新
        orderRepository.save(existingOrder);
        return orderMapper.toDTO(existingOrder);
    }
}
