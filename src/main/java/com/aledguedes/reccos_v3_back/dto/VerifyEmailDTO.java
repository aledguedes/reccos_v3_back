package com.aledguedes.reccos_v3_back.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

public record VerifyEmailDTO(
        @NotBlank(message = "Email is required") @Email(message = "Email must be valid") String email,

        @NotBlank(message = "Code is required") String code) {

}