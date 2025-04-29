package com.aledguedes.reccos_v3_back.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserInviteDTO(
        @NotBlank(message = "Email is required") @Email(message = "Email must be valid") String email,

        @NotBlank(message = "Role is required") String role) {

}