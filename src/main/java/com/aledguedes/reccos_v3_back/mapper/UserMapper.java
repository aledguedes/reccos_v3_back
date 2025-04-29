package com.aledguedes.reccos_v3_back.mapper;

import com.aledguedes.reccos_v3_back.dto.UserCompleteDTO;
import com.aledguedes.reccos_v3_back.dto.UserDTO;
import com.aledguedes.reccos_v3_back.model.Address;
import com.aledguedes.reccos_v3_back.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Mapping(target = "role", expression = "java(user.getRole() != null ? user.getRole().name() : null)")
    @Mapping(target = "federationId", expression = "java(user.getFederation() != null ? user.getFederation().getId() : null)")
    public abstract UserDTO toUserDTO(User user);

    @Mapping(target = "role", expression = "java(userDTO.role() != null ? com.aledguedes.reccos_v3_back.model.Role.valueOf(userDTO.role()) : null)")
    @Mapping(target = "federation", ignore = true)
    public abstract User toUser(UserDTO userDTO);

    public abstract void updateUserFromDTO(UserDTO userDTO, @MappingTarget User user);

    public void updateUserFromCompleteDTO(User user, UserCompleteDTO userCompleteDTO, PasswordEncoder passwordEncoder) {
        if (userCompleteDTO == null)
            return;

        user.setUsername(userCompleteDTO.username());
        user.setPassword(passwordEncoder.encode(userCompleteDTO.password()));
        user.setFullName(userCompleteDTO.fullName());
        user.setPhoneNumber(userCompleteDTO.phoneNumber());
        user.setCpf(userCompleteDTO.cpf());

        if (userCompleteDTO.address() != null) {
            Address address = new Address();
            address.setStreet(userCompleteDTO.address().street());
            address.setCity(userCompleteDTO.address().city());
            address.setState(userCompleteDTO.address().state());
            address.setZipCode(userCompleteDTO.address().postalCode());
            user.setAddress(address);
        }
    }
}