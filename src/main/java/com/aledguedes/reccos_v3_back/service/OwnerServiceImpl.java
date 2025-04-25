package com.aledguedes.reccos_v3_back.service;

import com.aledguedes.reccos_v3_back.dto.OwnerDTO;
import com.aledguedes.reccos_v3_back.mapper.OwnerMapper;
import com.aledguedes.reccos_v3_back.model.Owner;
import com.aledguedes.reccos_v3_back.repository.OwnerRepository;
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
        // Valida o limite de 2 OWNERs
        if (ownerRepository.count() >= 2) {
            throw new RuntimeException("Cannot create more than 2 owners");
        }

        // Verifica se o email ou username já existe
        if (ownerRepository.findByEmail(ownerDTO.email()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        if (ownerRepository.findByUsername(ownerDTO.username()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Cria um novo Owner com a senha criptografada
        Owner owner = new Owner();
        owner.setEmail(ownerDTO.email());
        owner.setUsername(ownerDTO.username());
        owner.setPassword(passwordEncoder.encode(ownerDTO.password()));

        // Log para depuração
        System.out.println("Owner before save: " + owner.toString());
        System.out.println("OwnerDTO: " + ownerDTO.toString());
        System.out.println("Mapped OwnerDTO: " + ownerMapper.toOwnerDTO(owner).toString());

        // Salva o OWNER
        Owner savedOwner = ownerRepository.save(owner);
        return ownerMapper.toOwnerDTO(savedOwner);
    }
}