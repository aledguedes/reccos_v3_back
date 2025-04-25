package com.aledguedes.reccos_v3_back.dto;

import com.aledguedes.reccos_v3_back.model.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode
public class UserDTO {
        private UUID id;

        @NotBlank(message = "Username is required")
        private String username;

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        private String email;

        @NotBlank(message = "Password is required")
        private String password;

        @NotBlank(message = "Role is required")
        private String role;

        private UUID federationId;
        private boolean emailVerified;
        private String emailVerificationCode;
        private Long emailVerificationCodeExpiry;
        private String fullName;
        private String phoneNumber;
        private String cpf;
        private Address address;
}