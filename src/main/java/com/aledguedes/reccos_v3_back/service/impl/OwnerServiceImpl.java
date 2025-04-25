package com.aledguedes.reccos_v3_back.service.impl;

import com.aledguedes.reccos_v3_back.dto.OwnerDTO;
import com.aledguedes.reccos_v3_back.exception.DuplicateResourceException;
import com.aledguedes.reccos_v3_back.mapper.OwnerMapper;
import com.aledguedes.reccos_v3_back.model.Owner;
import com.aledguedes.reccos_v3_back.repository.OwnerRepository;
import com.aledguedes.reccos_v3_back.service.OwnerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class OwnerServiceImpl implements OwnerService {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private OwnerMapper ownerMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public OwnerDTO createOwner(OwnerDTO ownerDTO) {
        if (ownerRepository.count() >= 2) {
            throw new RuntimeException("Cannot create more than 2 owners");
        }

        if (ownerRepository.findByEmail(ownerDTO.email()).isPresent()) {
            throw new DuplicateResourceException("Email already exists");
        }
        if (ownerRepository.findByUsername(ownerDTO.username()).isPresent()) {
            throw new DuplicateResourceException("Username already exists");
        }

        Owner owner = new Owner();
        owner.setEmail(ownerDTO.email());
        owner.setUsername(ownerDTO.username());
        owner.setPassword(passwordEncoder.encode(ownerDTO.password()));
        owner.setRole(com.aledguedes.reccos_v3_back.model.Role.OWNER); // Definir a role padrão
        owner.setEmailVerified(true); // Definir emailVerified como falso por padrão

        Owner savedOwner = ownerRepository.save(owner);
        return ownerMapper.toOwnerDTO(savedOwner);
    }
}