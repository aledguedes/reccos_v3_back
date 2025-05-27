package com.aledguedes.reccos_v3_back.service.impl;

import com.aledguedes.reccos_v3_back.dto.FederationDTO;
import com.aledguedes.reccos_v3_back.dto.UserCompleteDTO;
import com.aledguedes.reccos_v3_back.dto.UserDTO;
import com.aledguedes.reccos_v3_back.dto.UserInviteDTO;
import com.aledguedes.reccos_v3_back.dto.UserRegisterDTO;
import com.aledguedes.reccos_v3_back.dto.VerifyEmailDTO;
import com.aledguedes.reccos_v3_back.mapper.UserMapper;
import com.aledguedes.reccos_v3_back.mapper.UserMapperHelper;
import com.aledguedes.reccos_v3_back.model.Federation;
import com.aledguedes.reccos_v3_back.model.Role;
import com.aledguedes.reccos_v3_back.model.User;
import com.aledguedes.reccos_v3_back.model.UserFederation;
import com.aledguedes.reccos_v3_back.model.VerificationCode;
import com.aledguedes.reccos_v3_back.repository.FederationRepository;
import com.aledguedes.reccos_v3_back.repository.UserFederationRepository;
import com.aledguedes.reccos_v3_back.repository.UserRepository;
import com.aledguedes.reccos_v3_back.repository.VerificationCodeRepository;
import com.aledguedes.reccos_v3_back.service.EmailService;
import com.aledguedes.reccos_v3_back.service.FederationService;
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

    private final UserMapper userMapper;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapperHelper userMapperHelper;
    private final FederationService federationService;
    private final FederationRepository federationRepository;
    private final UserFederationRepository userFederationRepository;
    private final VerificationCodeRepository verificationCodeRepository;

    public UserServiceImpl(UserRepository userRepository,
            VerificationCodeRepository verificationCodeRepository,
            FederationRepository federationRepository,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper,
            EmailService emailService,
            UserFederationRepository userFederationRepository,
            FederationService federationService,
            UserMapperHelper userMapperHelper) {
			this.userRepository = userRepository;
			this.verificationCodeRepository = verificationCodeRepository;
			this.federationRepository = federationRepository;
			this.passwordEncoder = passwordEncoder;
			this.userMapper = userMapper;
			this.emailService = emailService;
			this.userMapperHelper = userMapperHelper;
			this.userFederationRepository = userFederationRepository;
			this.federationService = federationService;
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
        System.out.println("registerUser: " + userRegisterDTO.email() + " -- " + userRegisterDTO.role());
        if (userRepository.findByEmail(userRegisterDTO.email()).isPresent()) {
            throw new IllegalArgumentException("E-mail already registered.");
        }

        try {
            Role.valueOf(userRegisterDTO.role());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + userRegisterDTO.role());
        }

        String code = String.format("%06d", new Random().nextInt(999999));

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(userRegisterDTO.email());
        verificationCode.setCode(code);
        verificationCode.setRole(userRegisterDTO.role());
        verificationCode.setUsed(false);
        verificationCode.setExpiresAt(LocalDateTime.now().plusMinutes(15)); // Define expiresAt como timestamp atual + 15 minutos

        verificationCodeRepository.save(verificationCode);

        System.out.println(verificationCode.getEmail());
        System.out.println(verificationCode.getRole());
        System.out.println(verificationCode.getId());

        emailService.sendVerificationCodeEmail(userRegisterDTO.email(), code);
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

        User user = new User();
        user.setEmail(email);
        user.setEmailVerified(true);
        userMapperHelper.updateUserFromCompleteDTO(user, userCompleteDTO);
        user.setRole(Role.valueOf(verificationCode.getRole()));
        userRepository.save(user);
        
        if (user.getRole() == Role.ADMIN) {
        	FederationDTO createdFederation = createAndAssociateFederation(user);
            System.out.println("User " + user.getId() + " associated with Federation " + createdFederation.id());
        }

        verificationCode.setUsed(true);
        verificationCodeRepository.delete(verificationCode);
    }
    
    private FederationDTO createAndAssociateFederation(User user) {
    	FederationDTO federationDTO = new FederationDTO(null, "Federação de " + user.getUsername(), "Federação criada automaticamente");
    	   var createdFederation = federationService.createFederation(federationDTO);

    	    Federation federation = new Federation();
    	    federation.setId(createdFederation.id());

    	    UserFederation userFederation = new UserFederation();
    	    userFederation.setUser(user);
    	    userFederation.setFederation(federation);
    	    userFederationRepository.save(userFederation);

    	    user.setFederation(federation);
    	    userRepository.save(user);

    	    return createdFederation;
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

        emailService.sendVerificationCodeEmail(userRegisterDTO.email(), code);
    }

    @Override
    public void inviteUserByAdmin(UserInviteDTO userInviteDTO) {
        if (userInviteDTO.role().equals(Role.OWNER.name()) || userInviteDTO.role().equals(Role.ADMIN.name())) {
            throw new IllegalArgumentException("ADMIN cannot invite OWNER or ADMIN roles.");
        }

        try {
            Role.valueOf(userInviteDTO.role());
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

        emailService.sendVerificationCodeEmail(userInviteDTO.email(), code);
    }
}