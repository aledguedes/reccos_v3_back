package com.aledguedes.reccos_v3_back.mapper;

import com.aledguedes.reccos_v3_back.dto.FederationDTO;
import com.aledguedes.reccos_v3_back.model.Federation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FederationMapper {
    Federation toFederation(FederationDTO federationDTO);
    FederationDTO toFederationDTO(Federation federation);
}