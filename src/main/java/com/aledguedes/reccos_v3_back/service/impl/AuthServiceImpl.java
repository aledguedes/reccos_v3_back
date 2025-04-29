package com.aledguedes.reccos_v3_back.service.impl;

import com.aledguedes.reccos_v3_back.config.JwtTokenProvider;
import com.aledguedes.reccos_v3_back.dto.LoginRequestDTO;
import com.aledguedes.reccos_v3_back.dto.LoginResponseDTO;
import com.aledguedes.reccos_v3_back.exception.InvalidCredentialsException;
import com.aledguedes.reccos_v3_back.model.Owner;
import com.aledguedes.reccos_v3_back.repository.OwnerRepository;
import com.aledguedes.reccos_v3_back.service.AuthService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(OwnerRepository ownerRepository, PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider) {
        this.ownerRepository = ownerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Owner owner = ownerRepository.findByEmail(loginRequestDTO.email())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(loginRequestDTO.password(), owner.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtTokenProvider.generateToken(owner.getEmail());
        return new LoginResponseDTO(token, owner.getEmail(), owner.getRole().name());
    }
}