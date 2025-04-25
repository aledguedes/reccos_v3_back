package com.aledguedes.reccos_v3_back.controller;

import com.aledguedes.reccos_v3_back.dto.OwnerDTO;
import com.aledguedes.reccos_v3_back.service.OwnerService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")

public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @PostMapping("/create-owner")
    public ResponseEntity<OwnerDTO> createOwner(@Valid @RequestBody OwnerDTO ownerDTO) {
        OwnerDTO createdOwner = ownerService.createOwner(ownerDTO);
        return ResponseEntity.ok(createdOwner);
    }
}