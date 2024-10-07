package com.akobryn.crm.service.impl;

import com.akobryn.crm.dto.ClientDTO;
import com.akobryn.crm.entity.Client;
import com.akobryn.crm.entity.Contact;
import com.akobryn.crm.exceptions.CRMExceptions;
import com.akobryn.crm.mapper.ClientMapper;
import com.akobryn.crm.repository.ClientRepository;
import com.akobryn.crm.repository.ContactRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@Transactional
class ClientServiceIntegrationTest {

    @Autowired
    private ClientServiceImpl clientService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ClientMapper clientMapper;

    private Client client;

    private Contact contact;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setCompanyName("Test Company");
        client.setAddress("123 Test St");
        client.setIndustry("Industry");
        clientRepository.save(client);

        contact = new Contact();
        contact.setClient(client);
        contact.setEmail("test@test");
        contact.setFirstName("test");
        contact.setLastName("test");
        contact.setPhone("test");
        contactRepository.save(contact);
    }

    @Test
    void getClientById_shouldReturnClientDTO() {
        ClientDTO result = clientService.getClientById(client.getId());

        assertEquals(client.getCompanyName(), result.getCompanyName());
    }

    @Test
    void deleteClient_shouldRemoveClient() {
        clientService.deleteClient(client.getId());
        assertThrows(CRMExceptions.clientNotFound(client.getId()).getClass(), () -> clientService.getClientById(client.getId()));
    }

    @Test
    void searchClients_shouldReturnFilteredClients() {
        List<ClientDTO> result = clientService.searchClients("Test Company", null, null);

        assertEquals(1, result.size());
        assertEquals(client.getCompanyName(), result.get(0).getCompanyName());
    }
}
