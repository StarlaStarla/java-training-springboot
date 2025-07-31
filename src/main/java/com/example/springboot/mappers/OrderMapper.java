package com.example.springboot.mappers;

import com.example.springboot.dto.OrderDTO;
import com.example.springboot.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

// 定义 Mapper 接口
@Mapper(componentModel = "spring")
public interface OrderMapper {

    // 获取 Mapper 实例
//    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    // 映射 DTO -> 实体类
    @Mapping(target = "user", ignore = true)
    Order toEntity(OrderDTO dto);

    // 映射 实体类 -> DTO
    OrderDTO toDTO(Order entity);

    // 实体类列表 -> DTO 列表
    List<OrderDTO> toOrderDTOList(List<Order> orders);

    // DTO 列表 -> 实体类列表
    @Mapping(target = "user", ignore = true)
    List<Order> toOrderList(List<OrderDTO> orderDTOs);
}
