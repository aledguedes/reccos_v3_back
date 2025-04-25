package com.aledguedes.reccos_v3_back.controller;

import com.aledguedes.reccos_v3_back.dto.UserDTO;
import com.aledguedes.reccos_v3_back.dto.UserInviteDTO;
import com.aledguedes.reccos_v3_back.dto.UserRegisterDTO;
import com.aledguedes.reccos_v3_back.model.Role;
import com.aledguedes.reccos_v3_back.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.status(201).body(createdUser);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<List<UserDTO>> getAllUsers(
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) UUID federationId) {
        return ResponseEntity.ok(userService.getAllUsers(role, federationId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID id, @Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/create-admin")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<String> createAdminByOwner(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        userService.createAdminByOwner(userRegisterDTO);
        return ResponseEntity.status(201).body("Admin created. A verification code has been sent to the email.");
    }

    @PostMapping("/invite")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> inviteUserByAdmin(@Valid @RequestBody UserInviteDTO userInviteDTO) {
        userService.inviteUserByAdmin(userInviteDTO);
        return ResponseEntity.status(201)
                .body("User invited successfully. A verification code has been sent to the email.");
    }
}