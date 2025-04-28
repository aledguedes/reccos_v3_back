package com.aledguedes.reccos_v3_back.dto;

import com.aledguedes.reccos_v3_back.model.Address;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class UserCompleteDTO {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    private String fullName;
    private String phoneNumber;
    private String cpf;
    private Address address;
}