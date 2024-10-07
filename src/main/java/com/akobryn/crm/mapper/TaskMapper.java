package com.akobryn.crm.mapper;

import com.akobryn.crm.dto.TaskDTO;
import com.akobryn.crm.entity.Task;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {ContactMapper.class})
public interface TaskMapper {

    @Mapping(target = "contactId", source = "contact.id")
    TaskDTO toDTO(Task task);

    @Mapping(target = "contact.id", source = "contactId")
    Task toEntity(TaskDTO taskDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "contact.id", source = "contactId")
    void updateTaskFromDTO(TaskDTO taskDTO, @MappingTarget Task task);
}

