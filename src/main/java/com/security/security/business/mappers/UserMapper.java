package com.security.security.business.mappers;

import com.security.security.business.repository.model.UserDAO;
import com.security.security.dto.UserDTO;
import com.security.security.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "role", target = "role")
    UserDAO userToUserDAO(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "role", target = "role")
    User userDAOToUser(UserDAO userDAO);

    User userDTOToUser(UserDTO userDTO);
    UserDTO userDAOToUserDTO(UserDAO userDAO);
    UserDTO userToUserDTO(User user);
}