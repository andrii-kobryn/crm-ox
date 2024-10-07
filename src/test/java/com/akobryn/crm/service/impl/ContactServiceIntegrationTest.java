package com.akobryn.crm.service.impl;

import com.akobryn.crm.dto.ContactDTO;
import com.akobryn.crm.entity.Client;
import com.akobryn.crm.entity.Contact;
import com.akobryn.crm.exceptions.CRMBaseException;
import com.akobryn.crm.repository.ClientRepository;
import com.akobryn.crm.repository.ContactRepository;
import com.akobryn.crm.service.ContactService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
class ContactServiceIntegrationTest {
    @Autowired
    private ContactService contactService;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ClientRepository clientRepository;


    @Test
    void createContact_shouldSaveContact() {
        Client client = new Client();
        clientRepository.save(client);
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setFirstName("John");
        contactDTO.setLastName("Doe");
        contactDTO.setEmail("john.doe@example.com");
        contactDTO.setClientId(client.getId()); // встановлюємо ID клієнта

        ContactDTO savedContact = contactService.createContact(contactDTO);

        assertNotNull(savedContact);
        assertNotNull(savedContact.getId());
        assertEquals("John", savedContact.getFirstName());
        assertEquals("Doe", savedContact.getLastName());
    }

    @Test
    void updateContact_shouldUpdateExistingContact() {
        Client client = new Client();
        clientRepository.save(client);
        Contact contact = new Contact();
        contact.setFirstName("Jane");
        contact.setLastName("Doe");
        contact.setEmail("jane.doe@example.com");
        contact.setClient(client); // встановлюємо ID клієнта
        contactRepository.save(contact);

        Long contactId = contact.getId();
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setId(contactId);
        contactDTO.setFirstName("Updated Jane");
        contactDTO.setLastName("Updated Doe");

        ContactDTO updatedContact = contactService.updateContact(contactId, contactDTO);

        assertNotNull(updatedContact);
        assertEquals("Updated Jane", updatedContact.getFirstName());
        assertEquals("Updated Doe", updatedContact.getLastName());
    }

    @Test
    void getContactById_shouldReturnContact() {
        Client client = new Client();
        clientRepository.save(client);
        Contact contact = new Contact();
        contact.setFirstName("Mike");
        contact.setLastName("Smith");
        contact.setEmail("mike.smith@example.com");
        contact.setClient(client); // встановлюємо ID клієнта
        contactRepository.save(contact);

        Long contactId = contact.getId();

        ContactDTO contactDTO = contactService.getContactById(contactId);

        assertNotNull(contactDTO);
        assertEquals("Mike", contactDTO.getFirstName());
        assertEquals("Smith", contactDTO.getLastName());
    }

    @Test
    void deleteContact_shouldRemoveContact() {
        Client client = new Client();
        clientRepository.save(client);

        Contact contact = new Contact();
        contact.setFirstName("Chris");
        contact.setLastName("Brown");
        contact.setEmail("chris.brown@example.com");
        contact.setClient(client); // встановлюємо ID клієнта
        contactRepository.save(contact);

        Long contactId = contact.getId();

        Long deletedContactId = contactService.deleteContact(contactId);

        assertEquals(contactId, deletedContactId);
        assertThrows(CRMBaseException.class, () -> contactService.getContactById(contactId));
    }
}