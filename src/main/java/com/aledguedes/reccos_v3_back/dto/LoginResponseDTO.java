package com.aledguedes.reccos_v3_back.dto;

public record LoginResponseDTO(
                String token,
                String email,
                String role) {
}