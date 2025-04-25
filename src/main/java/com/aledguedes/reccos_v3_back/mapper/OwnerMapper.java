package com.aledguedes.reccos_v3_back.mapper;

import com.aledguedes.reccos_v3_back.dto.OwnerDTO;
import com.aledguedes.reccos_v3_back.model.Owner;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OwnerMapper {

    Owner toOwner(OwnerDTO ownerDTO);
    OwnerDTO toOwnerDTO(Owner owner);
}