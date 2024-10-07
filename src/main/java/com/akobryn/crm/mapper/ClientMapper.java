package com.akobryn.crm.mapper;

import com.akobryn.crm.dto.ClientDTO;
import com.akobryn.crm.entity.Client;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {ContactMapper.class})
public interface ClientMapper {

    @Mapping(target = "contacts", source = "contacts")
    ClientDTO toDTO(Client client);

    @Mapping(target = "contacts", source = "contacts")
    Client toEntity(ClientDTO clientDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateClientFromDTO(ClientDTO clientDTO, @MappingTarget Client client);
}

