package com.aledguedes.reccos_v3_back.dto;

public record AddressDTO(
        String street,
        String city,
        String state,
        String zipCode) {
}