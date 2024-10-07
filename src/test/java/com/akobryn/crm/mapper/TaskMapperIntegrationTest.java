package com.akobryn.crm.mapper;

import com.akobryn.crm.dto.TaskDTO;
import com.akobryn.crm.entity.Contact;
import com.akobryn.crm.entity.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TaskMapperIntegrationTest {

    @Autowired
    private TaskMapper taskMapper;

    @Test
    void testToDTO() {
        Task task = new Task();
        Contact contact = new Contact();
        contact.setId(1L);
        task.setId(1L);
        task.setContact(contact);

        TaskDTO taskDTO = taskMapper.toDTO(task);

        assertEquals(task.getId(), taskDTO.getId());
        assertEquals(task.getContact().getId(), taskDTO.getContactId());
    }

    @Test
    void testToEntity() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(1L);
        taskDTO.setContactId(1L);

        Task task = taskMapper.toEntity(taskDTO);

        assertEquals(taskDTO.getId(), task.getId());
        assertEquals(taskDTO.getContactId(), task.getContact().getId());
    }
}