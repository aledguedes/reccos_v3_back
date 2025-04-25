package com.aledguedes.reccos_v3_back.service;

import com.aledguedes.reccos_v3_back.dto.UserDTO;
import com.aledguedes.reccos_v3_back.exception.DuplicateResourceException;
import com.aledguedes.reccos_v3_back.exception.NotFoundException;
import com.aledguedes.reccos_v3_back.mapper.UserMapper;
import com.aledguedes.reccos_v3_back.model.Role;
import com.aledguedes.reccos_v3_back.model.User;
import com.aledguedes.reccos_v3_back.repository.UserFederationRepository;
import com.aledguedes.reccos_v3_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFederationRepository userFederationRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.email()).isPresent()) {
            throw new DuplicateResourceException("Email already exists");
        }
        if (userRepository.findByUsername(userDTO.username()).isPresent()) {
            throw new DuplicateResourceException("Username already exists");
        }

        User user = new User();
        user.setEmail(userDTO.email());
        user.setUsername(userDTO.username());
        user.setPassword(passwordEncoder.encode(userDTO.password()));
        user.setRole(userDTO.role());

        User savedUser = userRepository.save(user);
        return userMapper.toUserDTO(savedUser);
    }

    @Override
    public List<UserDTO> getAllUsers(Role role, UUID federationId) {
        List<User> users;
        if (federationId != null) {
            users = userFederationRepository.findByFederationId(federationId).stream()
                    .map(userFederation -> userFederation.getUser())
                    .collect(Collectors.toList());
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
                .orElseThrow(() -> new NotFoundException("User not found"));
        return userMapper.toUserDTO(user);
    }

    @Override
    public UserDTO updateUser(UUID id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!user.getEmail().equals(userDTO.email()) && userRepository.findByEmail(userDTO.email()).isPresent()) {
            throw new DuplicateResourceException("Email already exists");
        }
        if (!user.getUsername().equals(userDTO.username())
                && userRepository.findByUsername(userDTO.username()).isPresent()) {
            throw new DuplicateResourceException("Username already exists");
        }

        user.setEmail(userDTO.email());
        user.setUsername(userDTO.username());
        if (userDTO.password() != null && !userDTO.password().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.password()));
        }
        user.setRole(userDTO.role());

        User updatedUser = userRepository.save(user);
        return userMapper.toUserDTO(updatedUser);
    }

    @Override
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        userRepository.delete(user);
    }
}