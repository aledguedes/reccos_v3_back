package com.aledguedes.reccos_v3_back.service;

import com.aledguedes.reccos_v3_back.dto.FederationDTO;
import com.aledguedes.reccos_v3_back.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserFederationService {
    void associateUserToFederation(UUID userId, UUID federationId);
    void removeUserFromFederation(UUID userId, UUID federationId);
    List<FederationDTO> getFederationsByUser(UUID userId);
    List<UserDTO> getUsersByFederation(UUID federationId);
}