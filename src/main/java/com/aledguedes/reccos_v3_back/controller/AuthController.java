package com.aledguedes.reccos_v3_back.controller;

import com.aledguedes.reccos_v3_back.dto.LoginRequestDTO;
import com.aledguedes.reccos_v3_back.dto.LoginResponseDTO;
import com.aledguedes.reccos_v3_back.dto.UserCompleteDTO;
import com.aledguedes.reccos_v3_back.dto.UserRegisterDTO;
import com.aledguedes.reccos_v3_back.dto.VerifyEmailDTO;
import com.aledguedes.reccos_v3_back.service.AuthService;
import com.aledguedes.reccos_v3_back.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO response = authService.login(loginRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        userService.registerUser(userRegisterDTO);
        return ResponseEntity.ok("User registered. A verification code has been sent to the email.");
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@Valid @RequestBody VerifyEmailDTO verifyEmailDTO) {
        userService.verifyEmail(verifyEmailDTO);
        return ResponseEntity.ok("Email verified successfully");
    }

    @PostMapping("/complete-registration")
    public ResponseEntity<String> completeRegistration(@RequestParam String email,
            @Valid @RequestBody UserCompleteDTO userCompleteDTO) {
        userService.completeRegistration(email, userCompleteDTO);
        return ResponseEntity.ok("Registration completed successfully");
    }
}