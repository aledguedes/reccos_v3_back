package com.aledguedes.reccos_v3_back.service.impl;

import com.aledguedes.reccos_v3_back.dto.FederationDTO;
import com.aledguedes.reccos_v3_back.exception.DuplicateResourceException;
import com.aledguedes.reccos_v3_back.exception.NotFoundException;
import com.aledguedes.reccos_v3_back.mapper.FederationMapper;
import com.aledguedes.reccos_v3_back.model.Federation;
import com.aledguedes.reccos_v3_back.repository.FederationRepository;
import com.aledguedes.reccos_v3_back.service.FederationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FederationServiceImpl implements FederationService {

    @Autowired
    private FederationRepository federationRepository;

    @Autowired
    private FederationMapper federationMapper;

    @Override
    public FederationDTO createFederation(FederationDTO federationDTO) {
        if (federationRepository.findByName(federationDTO.name()).isPresent()) {
            throw new DuplicateResourceException("Federation name already exists");
        }

        Federation federation = new Federation();
        federation.setName(federationDTO.name());
        federation.setDescription(federationDTO.description());

        Federation savedFederation = federationRepository.save(federation);
        return federationMapper.toFederationDTO(savedFederation);
    }

    @Override
    public List<FederationDTO> getAllFederations(String name) {
        List<Federation> federations;
        if (name != null && !name.isEmpty()) {
            federations = federationRepository.findAll().stream()
                    .filter(federation -> federation.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        } else {
            federations = federationRepository.findAll();
        }
        return federations.stream().map(federationMapper::toFederationDTO).collect(Collectors.toList());
    }

    @Override
    public FederationDTO getFederationById(UUID id) {
        Federation federation = federationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Federation not found"));
        return federationMapper.toFederationDTO(federation);
    }

    @Override
    public FederationDTO updateFederation(UUID id, FederationDTO federationDTO) {
        Federation federation = federationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Federation not found"));

        if (!federation.getName().equals(federationDTO.name())
                && federationRepository.findByName(federationDTO.name()).isPresent()) {
            throw new DuplicateResourceException("Federation name already exists");
        }

        federation.setName(federationDTO.name());
        federation.setDescription(federationDTO.description());

        Federation updatedFederation = federationRepository.save(federation);
        return federationMapper.toFederationDTO(updatedFederation);
    }

    @Override
    public void deleteFederation(UUID id) {
        Federation federation = federationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Federation not found"));
        federationRepository.delete(federation);
    }
}