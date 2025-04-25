package com.aledguedes.reccos_v3_back.dto;

import com.aledguedes.reccos_v3_back.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserDTO(
        UUID id,

        @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,

        @NotBlank(message = "Password is required") String password,

        @NotBlank(message = "Username is required") String username,

        @NotNull(message = "Role is required") Role role) {
}