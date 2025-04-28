package com.aledguedes.reccos_v3_back.service;

import com.aledguedes.reccos_v3_back.dto.FederationDTO;

import java.util.List;
import java.util.UUID;

public interface FederationService {
    FederationDTO createFederation(FederationDTO federationDTO);
    List<FederationDTO> getAllFederations(String name);
    FederationDTO getFederationById(UUID id);
    FederationDTO updateFederation(UUID id, FederationDTO federationDTO);
    void deleteFederation(UUID id);
}