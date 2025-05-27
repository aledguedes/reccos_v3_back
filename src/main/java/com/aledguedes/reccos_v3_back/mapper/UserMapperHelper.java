package com.aledguedes.reccos_v3_back.mapper;

import com.aledguedes.reccos_v3_back.dto.UserCompleteDTO;
import com.aledguedes.reccos_v3_back.model.Address;
import com.aledguedes.reccos_v3_back.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapperHelper {

    private final PasswordEncoder passwordEncoder;

    public UserMapperHelper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void updateUserFromCompleteDTO(User user, UserCompleteDTO dto) {
        if (dto == null) return;

        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setFullName(dto.fullName());
        user.setPhoneNumber(dto.phoneNumber());
        user.setCpf(dto.cpf());

        if (dto.address() != null) {
            Address address = new Address();
            address.setStreet(dto.address().street());
            address.setCity(dto.address().city());
            address.setState(dto.address().state());
            address.setZipCode(dto.address().zipCode());
            user.setAddress(address);
        }
    }
}
