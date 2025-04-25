package com.aledguedes.reccos_v3_back.controller;

import com.aledguedes.reccos_v3_back.dto.FederationDTO;
import com.aledguedes.reccos_v3_back.dto.UserCompleteDTO;
import com.aledguedes.reccos_v3_back.dto.UserDTO;
import com.aledguedes.reccos_v3_back.dto.UserRegisterDTO;
import com.aledguedes.reccos_v3_back.dto.VerifyEmailDTO;
import com.aledguedes.reccos_v3_back.service.FederationService;
import com.aledguedes.reccos_v3_back.service.UserFederationService;
import com.aledguedes.reccos_v3_back.service.UserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/federations")
public class FederationController {

    @Autowired
    private FederationService federationService;

    @Autowired
    private UserFederationService userFederationService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<FederationDTO> createFederation(@Valid @RequestBody FederationDTO federationDTO) {
        FederationDTO createdFederation = federationService.createFederation(federationDTO);
        return ResponseEntity.status(201).body(createdFederation);
    }

    @GetMapping
    public ResponseEntity<List<FederationDTO>> getAllFederations(
            @RequestParam(required = false) String name) {
        return ResponseEntity.ok(federationService.getAllFederations(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FederationDTO> getFederationById(@PathVariable UUID id) {
        return ResponseEntity.ok(federationService.getFederationById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FederationDTO> updateFederation(@PathVariable UUID id,
            @Valid @RequestBody FederationDTO federationDTO) {
        return ResponseEntity.ok(federationService.updateFederation(id, federationDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFederation(@PathVariable UUID id) {
        federationService.deleteFederation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{federationId}/users")
    public ResponseEntity<List<UserDTO>> getUsersByFederation(@PathVariable UUID federationId) {
        return ResponseEntity.ok(userFederationService.getUsersByFederation(federationId));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        userService.registerUser(userRegisterDTO);
        return ResponseEntity.ok("User registered. A verification code has been sent to the email.");
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestBody VerifyEmailDTO verifyEmailDTO) {
        userService.verifyEmail(verifyEmailDTO);
        return ResponseEntity.ok("Email verified successfully");
    }

    @PostMapping("/complete-registration")
    public ResponseEntity<String> completeRegistration(@RequestParam String email,
            @RequestBody UserCompleteDTO userCompleteDTO) {
        userService.completeRegistration(email, userCompleteDTO);
        return ResponseEntity.ok("Registration completed successfully");
    }
}