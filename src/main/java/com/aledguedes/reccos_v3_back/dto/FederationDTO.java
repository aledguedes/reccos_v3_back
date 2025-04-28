package com.aledguedes.reccos_v3_back.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record FederationDTO(
                UUID id,

                @NotBlank(message = "Name is required") String name,
                String description) {
}