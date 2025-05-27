package com.aledguedes.reccos_v3_back.mapper;

import com.aledguedes.reccos_v3_back.dto.AddressDTO;
import com.aledguedes.reccos_v3_back.dto.UserDTO;
import com.aledguedes.reccos_v3_back.model.Address;
import com.aledguedes.reccos_v3_back.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", expression = "java(user.getRole() != null ? user.getRole().name() : null)")
    @Mapping(target = "federationId", expression = "java(user.getFederation() != null ? user.getFederation().getId() : null)")
    @Mapping(target = "address", source = "address")
    UserDTO toUserDTO(User user);

    @Mapping(target = "role", expression = "java(userDTO.role() != null ? com.aledguedes.reccos_v3_back.model.Role.valueOf(userDTO.role()) : null)")
    @Mapping(target = "federation", ignore = true)
    @Mapping(target = "address", source = "address")
    User toUser(UserDTO userDTO);

    void updateUserFromDTO(UserDTO userDTO, @MappingTarget User user);

    default AddressDTO toAddressDTO(Address address) {
        if (address == null) return null;
        return new AddressDTO(
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getZipCode()
        );
    }

    default Address toAddress(AddressDTO dto) {
        if (dto == null) return null;
        Address address = new Address();
        address.setStreet(dto.street());
        address.setCity(dto.city());
        address.setState(dto.state());
        address.setZipCode(dto.zipCode());
        return address;
    }
}

