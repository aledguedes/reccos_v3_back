package com.aledguedes.reccos_v3_back.service;

import com.aledguedes.reccos_v3_back.dto.UserDTO;
import com.aledguedes.reccos_v3_back.model.Role;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    List<UserDTO> getAllUsers(Role role, UUID federationId);
    UserDTO getUserById(UUID id);
    UserDTO updateUser(UUID id, UserDTO userDTO);
    void deleteUser(UUID id);
}