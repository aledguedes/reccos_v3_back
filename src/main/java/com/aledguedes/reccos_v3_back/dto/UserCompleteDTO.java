package com.aledguedes.reccos_v3_back.dto;

import jakarta.validation.constraints.NotBlank;

public record UserCompleteDTO(
        @NotBlank(message = "Username is required") String username,

        @NotBlank(message = "Password is required") String password,

        String fullName,
        String phoneNumber,
        String cpf,
        AddressDTO address) {
}