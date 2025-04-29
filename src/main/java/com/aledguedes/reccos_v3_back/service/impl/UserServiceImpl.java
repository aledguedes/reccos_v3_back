package com.aledguedes.reccos_v3_back.service.impl;

import com.aledguedes.reccos_v3_back.dto.UserCompleteDTO;
import com.aledguedes.reccos_v3_back.dto.UserDTO;
import com.aledguedes.reccos_v3_back.dto.UserInviteDTO;
import com.aledguedes.reccos_v3_back.dto.UserRegisterDTO;
import com.aledguedes.reccos_v3_back.dto.VerifyEmailDTO;
import com.aledguedes.reccos_v3_back.mapper.UserMapper;
import com.aledguedes.reccos_v3_back.model.Federation;
import com.aledguedes.reccos_v3_back.model.Role;
import com.aledguedes.reccos_v3_back.model.User;
import com.aledguedes.reccos_v3_back.model.VerificationCode;
import com.aledguedes.reccos_v3_back.repository.FederationRepository;
import com.aledguedes.reccos_v3_back.repository.UserRepository;
import com.aledguedes.reccos_v3_back.repository.VerificationCodeRepository;
import com.aledguedes.reccos_v3_back.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final FederationRepository federationRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository,
            VerificationCodeRepository verificationCodeRepository,
            FederationRepository federationRepository,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper) {
        this.userRepository = userRepository;
        this.verificationCodeRepository = verificationCodeRepository;
        this.federationRepository = federationRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.email()).isPresent()) {
            throw new IllegalArgumentException("E-mail already registered.");
        }
        if (userRepository.findByUsername(userDTO.username()).isPresent()) {
            throw new IllegalArgumentException("Username already taken.");
        }

        try {
            if (userDTO.role() != null) {
                Role.valueOf(userDTO.role()); // Valida o role
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + userDTO.role());
        }

        User user = userMapper.toUser(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.password()));
        user.setEmailVerified(true); // Assume que é um cadastro direto (ex.: por admin)

        if (userDTO.federationId() != null) {
            Federation federation = federationRepository.findById(userDTO.federationId())
                    .orElseThrow(() -> new IllegalArgumentException("Federation not found."));
            user.setFederation(federation);
        }

        User savedUser = userRepository.save(user);
        return userMapper.toUserDTO(savedUser);
    }

    @Override
    public List<UserDTO> getAllUsers(Role role, UUID federationId) {
        List<User> users;
        if (role != null && federationId != null) {
            users = userRepository.findByFederationIdAndRole(federationId, role);
        } else if (role != null) {
            users = userRepository.findByRole(role);
        } else {
            users = userRepository.findAll();
        }
        return users.stream().map(userMapper::toUserDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        return userMapper.toUserDTO(user);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        if (!user.getEmail().equals(userDTO.email()) && userRepository.findByEmail(userDTO.email()).isPresent()) {
            throw new IllegalArgumentException("E-mail already registered.");
        }
        if (!user.getUsername().equals(userDTO.username())
                && userRepository.findByUsername(userDTO.username()).isPresent()) {
            throw new IllegalArgumentException("Username already taken.");
        }

        try {
            if (userDTO.role() != null) {
                Role.valueOf(userDTO.role()); // Valida o role
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + userDTO.role());
        }

        userMapper.updateUserFromDTO(userDTO, user);

        if (userDTO.password() != null && !userDTO.password().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.password()));
        }

        if (userDTO.federationId() != null) {
            Federation federation = federationRepository.findById(userDTO.federationId())
                    .orElseThrow(() -> new IllegalArgumentException("Federation not found."));
            user.setFederation(federation);
        } else {
            user.setFederation(null);
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toUserDTO(updatedUser);
    }

    @Override
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found.");
        }
        userRepository.deleteById(id);
    }

    @Override
    public void registerUser(UserRegisterDTO userRegisterDTO) {
        if (userRepository.findByEmail(userRegisterDTO.email()).isPresent()) {
            throw new IllegalArgumentException("E-mail already registered.");
        }

        try {
            Role.valueOf(userRegisterDTO.role()); // Valida o role
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + userRegisterDTO.role());
        }

        String code = String.format("%06d", new Random().nextInt(999999));

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(userRegisterDTO.email());
        verificationCode.setCode(code);
        verificationCode.setRole(userRegisterDTO.role());
        verificationCode.setUsed(false);
        verificationCodeRepository.save(verificationCode);
    }

    @Override
    public void verifyEmail(VerifyEmailDTO verifyEmailDTO) {
        VerificationCode verificationCode = verificationCodeRepository
                .findByEmailAndCodeAndUsedFalse(verifyEmailDTO.email(), verifyEmailDTO.code())
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired code."));

        if (verificationCode.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Code expired.");
        }

        verificationCode.setUsed(true);
        verificationCodeRepository.save(verificationCode);
    }

    @Override
    public void completeRegistration(String email, UserCompleteDTO userCompleteDTO) {
        VerificationCode verificationCode = verificationCodeRepository
                .findByEmailAndUsedTrue(email)
                .orElseThrow(() -> new IllegalArgumentException("Email not verified."));

        if (userRepository.findByUsername(userCompleteDTO.username()).isPresent()) {
            throw new IllegalArgumentException("Username already taken.");
        }

        User user = new User();
        user.setEmail(email);
        user.setEmailVerified(true);
        userMapper.updateUserFromCompleteDTO(user, userCompleteDTO, passwordEncoder);
        user.setRole(Role.valueOf(verificationCode.getRole()));
        userRepository.save(user);

        verificationCodeRepository.delete(verificationCode);
    }

    @Override
    public void createAdminByOwner(UserRegisterDTO userRegisterDTO) {
        if (!userRegisterDTO.role().equals(Role.ADMIN.name())) {
            throw new IllegalArgumentException("Only ADMIN role can be created by OWNER.");
        }

        if (userRepository.findByEmail(userRegisterDTO.email()).isPresent()) {
            throw new IllegalArgumentException("E-mail already registered.");
        }

        String code = String.format("%06d", new Random().nextInt(999999));

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(userRegisterDTO.email());
        verificationCode.setCode(code);
        verificationCode.setRole(userRegisterDTO.role());
        verificationCode.setUsed(false);
        verificationCodeRepository.save(verificationCode);
    }

    @Override
    public void inviteUserByAdmin(UserInviteDTO userInviteDTO) {
        if (userInviteDTO.role().equals(Role.OWNER.name()) || userInviteDTO.role().equals(Role.ADMIN.name())) {
            throw new IllegalArgumentException("ADMIN cannot invite OWNER or ADMIN roles.");
        }

        try {
            Role.valueOf(userInviteDTO.role()); // Valida o role
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + userInviteDTO.role());
        }

        if (userRepository.findByEmail(userInviteDTO.email()).isPresent()) {
            throw new IllegalArgumentException("E-mail already registered.");
        }

        String code = String.format("%06d", new Random().nextInt(999999));

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(userInviteDTO.email());
        verificationCode.setCode(code);
        verificationCode.setRole(userInviteDTO.role());
        verificationCode.setUsed(false);
        verificationCodeRepository.save(verificationCode);

        // Futuramente: Enviar convite por e-mail com o código
    }
}