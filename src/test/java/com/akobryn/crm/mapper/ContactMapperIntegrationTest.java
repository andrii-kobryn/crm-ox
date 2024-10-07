package com.akobryn.crm.mapper;

import com.akobryn.crm.dto.ContactDTO;
import com.akobryn.crm.entity.Client;
import com.akobryn.crm.entity.Contact;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ContactMapperIntegrationTest {

    @Autowired
    private ContactMapper contactMapper;

    @Test
    void testToDTO() {
        Contact contact = new Contact();
        Client client = new Client();
        client.setId(1L);
        contact.setId(1L);
        contact.setClient(client);
        contact.setTasks(new HashSet<>());

        ContactDTO contactDTO = contactMapper.toDTO(contact);

        assertEquals(contact.getId(), contactDTO.getId());
        assertEquals(contact.getClient().getId(), contactDTO.getClientId());
        assertNotNull(contactDTO.getTasks());
    }

    @Test
    void testToEntity() {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setId(1L);
        contactDTO.setClientId(1L);
        contactDTO.setTasks(new HashSet<>());

        Contact contact = contactMapper.toEntity(contactDTO);

        assertEquals(contactDTO.getId(), contact.getId());
        assertEquals(contactDTO.getClientId(), contact.getClient().getId());
        assertNotNull(contact.getTasks());
    }

    @Test
    void testUpdateContactFromDTO() {
        Contact contact = new Contact();
        contact.setId(1L);
        Client client = new Client();
        client.setId(1L);
        contact.setClient(client);

        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setClientId(2L);

        contactMapper.updateContactFromDTO(contactDTO, contact);

        assertEquals(contactDTO.getClientId(), contact.getClient().getId());
    }

}