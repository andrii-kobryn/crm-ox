package com.akobryn.crm.mapper;

import com.akobryn.crm.dto.ClientDTO;
import com.akobryn.crm.dto.ContactDTO;
import com.akobryn.crm.entity.Client;
import com.akobryn.crm.entity.Contact;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ClientMapperIntegrationTest {

    @Autowired
    private ClientMapper clientMapper;

    @Test
    public void testToDTO() {
        Set<Contact> contacts = new HashSet<>();
        contacts.add(new Contact());

        Client client = new Client();
        client.setId(1L);
        client.setCompanyName("Test Company");
        client.setIndustry("Test Industry");
        client.setAddress("123 Test St");
        client.setContacts(contacts);

        ClientDTO clientDTO = clientMapper.toDTO(client);

        assertEquals(client.getId(), clientDTO.getId());
        assertEquals(client.getCompanyName(), clientDTO.getCompanyName());
        assertEquals(client.getIndustry(), clientDTO.getIndustry());
        assertEquals(client.getAddress(), clientDTO.getAddress());
        assertEquals(client.getContacts().size(), clientDTO.getContacts().size());
    }

    @Test
    public void testToEntity() {
        Set<ContactDTO> contactDTOs = new HashSet<>();
        contactDTOs.add(new ContactDTO());

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(1L);
        clientDTO.setCompanyName("Test Company");
        clientDTO.setIndustry("Test Industry");
        clientDTO.setAddress("123 Test St");
        clientDTO.setContacts(contactDTOs);

        Client client = clientMapper.toEntity(clientDTO);

        assertEquals(clientDTO.getId(), client.getId());
        assertEquals(clientDTO.getCompanyName(), client.getCompanyName());
        assertEquals(clientDTO.getIndustry(), client.getIndustry());
        assertEquals(clientDTO.getAddress(), client.getAddress());
        assertEquals(clientDTO.getContacts().size(), client.getContacts().size());
    }

    @Test
    public void testUpdateClientFromDTO() {
        Client client = new Client();
        client.setId(1L);
        client.setCompanyName("Old Company");
        client.setIndustry("Old Industry");
        client.setAddress("Old Address");

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setCompanyName("New Company");
        clientDTO.setIndustry("New Industry");
        clientDTO.setAddress("New Address");

        clientMapper.updateClientFromDTO(clientDTO, client);

        assertEquals("New Company", client.getCompanyName());
        assertEquals("New Industry", client.getIndustry());
        assertEquals("New Address", client.getAddress());
    }
}