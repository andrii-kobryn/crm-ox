package com.akobryn.crm.mapper;

import com.akobryn.crm.dto.InteractionDTO;
import com.akobryn.crm.entity.Interaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InteractionMapper {

    @Mapping(source = "clientId", target = "clientId")
    @Mapping(source = "contactId", target = "contactId")
    InteractionDTO toDto(Interaction interaction);

    @Mapping(source = "clientId", target = "clientId")
    @Mapping(source = "contactId", target = "contactId")
    Interaction toEntity(InteractionDTO interactionDTO);
}


