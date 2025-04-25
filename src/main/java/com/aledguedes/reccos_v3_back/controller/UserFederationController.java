package com.aledguedes.reccos_v3_back.controller;

import com.aledguedes.reccos_v3_back.dto.FederationDTO;
import com.aledguedes.reccos_v3_back.service.UserFederationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users/{userId}/federations")
public class UserFederationController {

    @Autowired
    private UserFederationService userFederationService;

    @PostMapping("/{federationId}")
    public ResponseEntity<Void> associateUserToFederation(
            @PathVariable UUID userId,
            @PathVariable UUID federationId) {
        userFederationService.associateUserToFederation(userId, federationId);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{federationId}")
    public ResponseEntity<Void> removeUserFromFederation(
            @PathVariable UUID userId,
            @PathVariable UUID federationId) {
        userFederationService.removeUserFromFederation(userId, federationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<FederationDTO>> getFederationsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(userFederationService.getFederationsByUser(userId));
    }
}