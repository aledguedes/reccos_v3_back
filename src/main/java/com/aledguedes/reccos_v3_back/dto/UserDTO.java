package com.aledguedes.reccos_v3_back.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserDTO(
                UUID id,

                @NotBlank(message = "Username is required") String username,

                @NotBlank(message = "Email is required") @Email(message = "Email must be valid") String email,

                @NotBlank(message = "Password is required") String password,

                @NotBlank(message = "Role is required") String role,

                UUID federationId,
                boolean emailVerified,
                String emailVerificationCode,
                Long emailVerificationCodeExpiry,
                String fullName,
                String phoneNumber,
                String cpf,
                AddressDTO address,
                LocalDateTime createdAt,
                LocalDateTime updatedAt) {
}