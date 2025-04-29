package com.aledguedes.reccos_v3_back.service;

import com.aledguedes.reccos_v3_back.dto.UserCompleteDTO;
import com.aledguedes.reccos_v3_back.dto.UserDTO;
import com.aledguedes.reccos_v3_back.dto.UserInviteDTO;
import com.aledguedes.reccos_v3_back.dto.UserRegisterDTO;
import com.aledguedes.reccos_v3_back.dto.VerifyEmailDTO;
import com.aledguedes.reccos_v3_back.model.Role;

import java.util.List;
import java.util.UUID;

public interface UserService {
    // Métodos já existentes (que você tinha)
    UserDTO createUser(UserDTO userDTO);

    List<UserDTO> getAllUsers(Role role, UUID federationId);

    UserDTO getUserById(UUID id);

    UserDTO updateUser(UserDTO userDTO);

    void deleteUser(UUID id);

    void registerUser(UserRegisterDTO userRegisterDTO);

    void verifyEmail(VerifyEmailDTO verifyEmailDTO);

    void completeRegistration(String email, UserCompleteDTO userCompleteDTO);

    void createAdminByOwner(UserRegisterDTO userRegisterDTO);

    void inviteUserByAdmin(UserInviteDTO userInviteDTO);
}