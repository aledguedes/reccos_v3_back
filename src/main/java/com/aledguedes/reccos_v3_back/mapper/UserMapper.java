package com.aledguedes.reccos_v3_back.mapper;

import com.aledguedes.reccos_v3_back.dto.UserDTO;
import com.aledguedes.reccos_v3_back.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserDTO userDTO);
    UserDTO toUserDTO(User user);
}