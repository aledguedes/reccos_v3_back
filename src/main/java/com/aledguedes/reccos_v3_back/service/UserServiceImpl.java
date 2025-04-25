package com.aledguedes.reccos_v3_back.service;

import com.aledguedes.reccos_v3_back.dto.UserCompleteDTO;
import com.aledguedes.reccos_v3_back.dto.UserDTO;
import com.aledguedes.reccos_v3_back.dto.UserInviteDTO;
import com.aledguedes.reccos_v3_back.dto.UserRegisterDTO;
import com.aledguedes.reccos_v3_back.dto.VerifyEmailDTO;
import com.aledguedes.reccos_v3_back.exception.BadRequestException;
import com.aledguedes.reccos_v3_back.exception.ConflictException;
import com.aledguedes.reccos_v3_back.exception.NotFoundException;
import com.aledguedes.reccos_v3_back.model.Federation;
import com.aledguedes.reccos_v3_back.model.Role;
import com.aledguedes.reccos_v3_back.model.User;
import com.aledguedes.reccos_v3_back.repository.FederationRepository;
import com.aledguedes.reccos_v3_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FederationRepository federationRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        if (userDTO == null) {
            throw new BadRequestException("User data is required");
        }

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new ConflictException("Email already exists: " + userDTO.getEmail());
        }
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new ConflictException("Username already exists: " + userDTO.getUsername());
        }

        try {
            Role.valueOf(userDTO.getRole());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role: " + userDTO.getRole());
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRole(Role.valueOf(userDTO.getRole()));
        if (userDTO.getFederationId() != null) {
            Federation federation = federationRepository.findById(userDTO.getFederationId())
                    .orElseThrow(
                            () -> new NotFoundException("Federation not found with ID: " + userDTO.getFederationId()));
            user.setFederation(federation);
        }
        user.setEmailVerified(userDTO.isEmailVerified());
        user.setEmailVerificationCode(userDTO.getEmailVerificationCode());
        user.setEmailVerificationCodeExpiry(userDTO.getEmailVerificationCodeExpiry());
        user.setFullName(userDTO.getFullName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setCpf(userDTO.getCpf());
        user.setAddress(userDTO.getAddress());

        user = userRepository.save(user);
        return mapToDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers(Role role, UUID federationId) {
        List<User> users;
        if (role != null && federationId != null) {
            users = userRepository.findByFederationIdAndRole(federationId, role);
        } else if (role != null) {
            users = userRepository.findByRole(role);
        } else if (federationId != null) {
            Federation federation = federationRepository.findById(federationId)
                    .orElseThrow(() -> new NotFoundException("Federation not found with ID: " + federationId));
            users = federation.getUsers();
        } else {
            users = userRepository.findAll();
        }
        return users.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(UUID id) {
        if (id == null) {
            throw new BadRequestException("User ID is required");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
        return mapToDTO(user);
    }

    @Override
    public UserDTO updateUser(UUID id, UserDTO userDTO) {
        if (id == null) {
            throw new BadRequestException("User ID is required");
        }
        if (userDTO == null) {
            throw new BadRequestException("User data is required");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));

        if (!user.getEmail().equals(userDTO.getEmail()) && userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new ConflictException("Email already exists: " + userDTO.getEmail());
        }
        if (!user.getUsername().equals(userDTO.getUsername())
                && userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new ConflictException("Username already exists: " + userDTO.getUsername());
        }

        try {
            Role.valueOf(userDTO.getRole());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role: " + userDTO.getRole());
        }

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRole(Role.valueOf(userDTO.getRole()));
        if (userDTO.getFederationId() != null) {
            Federation federation = federationRepository.findById(userDTO.getFederationId())
                    .orElseThrow(
                            () -> new NotFoundException("Federation not found with ID: " + userDTO.getFederationId()));
            user.setFederation(federation);
        } else {
            user.setFederation(null);
        }
        user.setEmailVerified(userDTO.isEmailVerified());
        user.setEmailVerificationCode(userDTO.getEmailVerificationCode());
        user.setEmailVerificationCodeExpiry(userDTO.getEmailVerificationCodeExpiry());
        user.setFullName(userDTO.getFullName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setCpf(userDTO.getCpf());
        user.setAddress(userDTO.getAddress());

        user = userRepository.save(user);
        return mapToDTO(user);
    }

    @Override
    public void deleteUser(UUID id) {
        if (id == null) {
            throw new BadRequestException("User ID is required");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
        if (user.getRole() == Role.ADMIN) {
            long adminCount = userRepository.findByFederationIdAndRole(user.getFederation().getId(), Role.ADMIN).size();
            if (adminCount <= 1) {
                throw new BadRequestException("Cannot delete the only ADMIN of the federation");
            }
        }
        userRepository.delete(user);
    }

    @Override
    public void registerUser(UserRegisterDTO userRegisterDTO) {
        if (userRegisterDTO == null) {
            throw new BadRequestException("User registration data is required");
        }
        if (userRepository.findByEmail(userRegisterDTO.getEmail()).isPresent()) {
            throw new ConflictException("Email already exists: " + userRegisterDTO.getEmail());
        }

        User user = new User();
        user.setEmail(userRegisterDTO.getEmail());
        user.setRole(Role.ADMIN);
        user.setEmailVerified(false);

        String verificationCode = generateVerificationCode();
        user.setEmailVerificationCode(verificationCode);
        user.setEmailVerificationCodeExpiry(Instant.now().plusSeconds(24 * 60 * 60).getEpochSecond());

        userRepository.save(user);

        String emailContent = "Seu código de validação é: " + verificationCode;
        emailService.sendEmail(user.getEmail(), "Valide seu e-mail", emailContent);
    }

    @Override
    public void verifyEmail(VerifyEmailDTO verifyEmailDTO) {
        if (verifyEmailDTO == null) {
            throw new BadRequestException("Verification data is required");
        }
        User user = userRepository.findByEmail(verifyEmailDTO.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found with email: " + verifyEmailDTO.getEmail()));

        if (user.isEmailVerified()) {
            throw new BadRequestException("Email already verified for user: " + user.getEmail());
        }

        if (!user.getEmailVerificationCode().equals(verifyEmailDTO.getCode())) {
            throw new BadRequestException("Invalid verification code for user: " + user.getEmail());
        }

        if (user.getEmailVerificationCodeExpiry() < Instant.now().getEpochSecond()) {
            throw new BadRequestException("Verification code expired for user: " + user.getEmail());
        }

        user.setEmailVerified(true);
        user.setEmailVerificationCode(null);
        user.setEmailVerificationCodeExpiry(null);
        userRepository.save(user);
    }

    @Override
    public void completeRegistration(String email, UserCompleteDTO userCompleteDTO) {
        if (email == null || email.isBlank()) {
            throw new BadRequestException("Email is required");
        }
        if (userCompleteDTO == null) {
            throw new BadRequestException("User completion data is required");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));

        if (!user.isEmailVerified()) {
            throw new BadRequestException("Email not verified for user: " + email);
        }

        if (userRepository.findByUsername(userCompleteDTO.getUsername()).isPresent()) {
            throw new ConflictException("Username already exists: " + userCompleteDTO.getUsername());
        }

        user.setUsername(userCompleteDTO.getUsername());
        user.setPassword(userCompleteDTO.getPassword());
        user.setFullName(userCompleteDTO.getFullName());
        user.setPhoneNumber(userCompleteDTO.getPhoneNumber());
        user.setCpf(userCompleteDTO.getCpf());
        user.setAddress(userCompleteDTO.getAddress());

        if (user.getRole() == Role.ADMIN) {
            Federation federation = new Federation();
            String federationName = "Federação de " + user.getUsername();
            if (federationRepository.findByName(federationName).isPresent()) {
                throw new ConflictException("Federation name already exists: " + federationName);
            }
            federation.setName(federationName);
            federation = federationRepository.save(federation);
            user.setFederation(federation);
        }

        userRepository.save(user);
    }

    @Override
    public void createAdminByOwner(UserRegisterDTO userRegisterDTO) {
        if (userRegisterDTO == null) {
            throw new BadRequestException("User registration data is required");
        }
        List<User> owners = userRepository.findByRole(Role.OWNER);
        if (owners.isEmpty()) {
            User user = new User();
            user.setEmail(userRegisterDTO.getEmail());
            user.setRole(Role.OWNER);
            user.setEmailVerified(true);
            user.setUsername(userRegisterDTO.getEmail().split("@")[0]);
            user.setPassword("defaultPassword");
            userRepository.save(user);
            return;
        }

        if (owners.size() >= 2) {
            throw new BadRequestException("Maximum number of OWNERs reached (limit: 2)");
        }

        if (userRepository.findByEmail(userRegisterDTO.getEmail()).isPresent()) {
            throw new ConflictException("Email already exists: " + userRegisterDTO.getEmail());
        }

        User user = new User();
        user.setEmail(userRegisterDTO.getEmail());
        user.setRole(Role.ADMIN);
        user.setEmailVerified(false);

        String verificationCode = generateVerificationCode();
        user.setEmailVerificationCode(verificationCode);
        user.setEmailVerificationCodeExpiry(Instant.now().plusSeconds(24 * 60 * 60).getEpochSecond());

        userRepository.save(user);

        String emailContent = "Seu código de validação é: " + verificationCode;
        emailService.sendEmail(user.getEmail(), "Valide seu e-mail", emailContent);
    }

    @Override
    public void inviteUserByAdmin(UserInviteDTO userInviteDTO) {
        if (userInviteDTO == null) {
            throw new BadRequestException("User invitation data is required");
        }
        if (userRepository.findByEmail(userInviteDTO.getEmail()).isPresent()) {
            throw new ConflictException("Email already exists: " + userInviteDTO.getEmail());
        }

        if (!List.of(Role.USER.name(), Role.TEAM_MANAGER.name(), Role.ASSISTANT_1.name(), Role.ASSISTANT_2.name())
                .contains(userInviteDTO.getRole())) {
            throw new BadRequestException("Invalid role for ADMIN invite: " + userInviteDTO.getRole());
        }

        User admin = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new NotFoundException("ADMIN not found with email: "
                        + SecurityContextHolder.getContext().getAuthentication().getName()));
        Federation federation = admin.getFederation();
        if (federation == null) {
            throw new BadRequestException("ADMIN is not associated with any federation");
        }

        if (userInviteDTO.getRole().equals(Role.ASSISTANT_1.name())) {
            long assistant1Count = userRepository.findByFederationIdAndRole(federation.getId(), Role.ASSISTANT_1)
                    .size();
            if (assistant1Count >= 1) {
                throw new BadRequestException("Maximum number of ASSISTANT_1 reached for this federation (limit: 1)");
            }
        }

        if (userInviteDTO.getRole().equals(Role.ASSISTANT_2.name())) {
            long assistant2Count = userRepository.findByFederationIdAndRole(federation.getId(), Role.ASSISTANT_2)
                    .size();
            if (assistant2Count >= 1) {
                throw new BadRequestException("Maximum number of ASSISTANT_2 reached for this federation (limit: 1)");
            }
        }

        User user = new User();
        user.setEmail(userInviteDTO.getEmail());
        user.setRole(Role.valueOf(userInviteDTO.getRole()));
        user.setFederation(federation);
        user.setEmailVerified(false);

        String verificationCode = generateVerificationCode();
        user.setEmailVerificationCode(verificationCode);
        user.setEmailVerificationCodeExpiry(Instant.now().plusSeconds(24 * 60 * 60).getEpochSecond());

        userRepository.save(user);

        String emailContent = "Seu código de validação é: " + verificationCode;
        emailService.sendEmail(user.getEmail(), "Valide seu e-mail", emailContent);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }

    private UserDTO mapToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole().name());
        userDTO.setFederationId(user.getFederation() != null ? user.getFederation().getId() : null);
        userDTO.setEmailVerified(user.isEmailVerified());
        userDTO.setEmailVerificationCode(user.getEmailVerificationCode());
        userDTO.setEmailVerificationCodeExpiry(user.getEmailVerificationCodeExpiry());
        userDTO.setFullName(user.getFullName());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setCpf(user.getCpf());
        userDTO.setAddress(user.getAddress());
        return userDTO;
    }
}