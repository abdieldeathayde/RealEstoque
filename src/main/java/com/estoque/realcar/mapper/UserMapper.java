package com.estoque.realcar.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.estoque.realcar.dto.UserDTO;
import com.estoque.realcar.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(UserDTO dto);

    UserDTO toDTO(User user);

    List<UserDTO> toDTOs(List<User> users);

    List<User> toEntities(List<UserDTO> dtos);
}