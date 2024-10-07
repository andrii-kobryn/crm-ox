package com.akobryn.crm.mapper;

import com.akobryn.crm.dto.ContactDTO;
import com.akobryn.crm.entity.Contact;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {TaskMapper.class})
public interface ContactMapper {
    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "tasks", source = "tasks")
    ContactDTO toDTO(Contact contact);

    @Mapping(target = "client.id", source = "clientId")
    @Mapping(target = "tasks", source = "tasks")
    Contact toEntity(ContactDTO contactDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "client.id", source = "clientId")
    void updateContactFromDTO(ContactDTO contactDTO, @MappingTarget Contact contact);
}

