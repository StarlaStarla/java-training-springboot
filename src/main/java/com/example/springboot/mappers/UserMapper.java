package com.example.springboot.mappers;

import com.example.springboot.dto.UserDTO;
import com.example.springboot.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

//componentModelde注解：作为 Spring Bean 被 Spring 容器管理，可通过 @Autowired 注入 Mapper 实例
@Mapper(componentModel = "spring")
public interface UserMapper {

    // 作用是获取由 MapStruct 自动生成的 OrderMapper 实现类（例如 OrderMapperImpl）的单例对象
//    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // 映射 DTO -> 实体类
    @Mapping(source = "orderIds", target = "orders", ignore = true)
    User toEntity(UserDTO dto);

    // 映射 实体类 -> DTO
    @Mapping(source = "orders", target = "orderIds", ignore = true)
    UserDTO toDTO(User entity);

    // 实体类列表 -> DTO 列表
    @Mapping(source = "orders", target = "orderIds", ignore = true)
    List<UserDTO> toUserDTOList(List<User> users);

    // DTO 列表 -> 实体类列表
    @Mapping(source = "orderIds", target = "orders", ignore = true)
    List<User> toUserList(List<UserDTO> userDTOs);
}
