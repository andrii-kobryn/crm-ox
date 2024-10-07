package com.akobryn.crm.service.impl;

import com.akobryn.crm.dto.ContactDTO;
import com.akobryn.crm.entity.Client;
import com.akobryn.crm.entity.Contact;
import com.akobryn.crm.exceptions.CRMBaseException;
import com.akobryn.crm.mapper.ContactMapper;
import com.akobryn.crm.repository.ContactRepository;
import com.akobryn.crm.service.InteractionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceUnitTest {
    @Mock
    private ContactRepository contactRepository;

    @Mock
    private ContactMapper contactMapper;

    @Mock
    private InteractionService interactionService;

    @InjectMocks
    private ContactServiceImpl contactService;

    @Test
    void updateContact_whenExists_shouldReturnUpdatedContactDTO() {
        Long contactId = 1L;
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setFirstName("Updated Name");
        contactDTO.setLastName("Updated Lastname");

        Contact existingContact = new Contact();
        existingContact.setId(contactId);
        existingContact.setFirstName("Old Name");
        existingContact.setLastName("Old Lastname");

        Contact updatedContact = new Contact();
        existingContact.setId(contactId);
        existingContact.setFirstName("Updated Name");
        existingContact.setLastName("Updated Lastname");

        Client client = new Client();
        client.setId(1L);
        existingContact.setClient(client);
        updatedContact.setClient(client);

        when(contactRepository.findById(contactId)).thenReturn(Optional.of(existingContact));
        when(contactRepository.save(any(Contact.class))).thenReturn(updatedContact);
        when(contactMapper.toDTO(any(Contact.class))).thenReturn(contactDTO);

        ContactDTO updatedContactDTO = contactService.updateContact(contactId, contactDTO);

        assertEquals("Updated Name", updatedContactDTO.getFirstName());
        assertEquals("Updated Lastname", updatedContactDTO.getLastName());

        verify(contactRepository).save(existingContact);
    }



    @Test
    void updateContact_whenNotExists_shouldThrowContactNotFoundException() {
        Long contactId = 1L;
        ContactDTO contactDTO = new ContactDTO();

        when(contactRepository.findById(contactId)).thenReturn(Optional.empty());

        CRMBaseException exception = assertThrows(CRMBaseException.class, () -> {
            contactService.updateContact(contactId, contactDTO);
        });
        assertEquals("Contact with id: " + contactId + " not found", exception.getMessage());
    }

    @Test
    void createContact_shouldSaveAndReturnContactDTO() {
        Client client = new Client();
        client.setId(1L);

        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setFirstName("Jane");
        contactDTO.setLastName("Doe");
        contactDTO.setEmail("jane.doe@example.com");

        Contact contactToSave = new Contact();
        contactToSave.setClient(client);
        contactToSave.setId(1L);

        when(contactMapper.toEntity(contactDTO)).thenReturn(contactToSave);
        when(contactRepository.save(contactToSave)).thenReturn(contactToSave);
        when(contactMapper.toDTO(contactToSave)).thenReturn(contactDTO);

        ContactDTO result = contactService.createContact(contactDTO);

        assertEquals(contactDTO.getFirstName(), result.getFirstName());
        verify(contactRepository).save(contactToSave);
        verify(interactionService).createInteraction(any()); // Перевірка виклику для створення взаємодії
    }

    @Test
    void deleteContact_whenExists_shouldReturnContactId() {
        Long contactId = 1L;
        Contact existingContact = new Contact();
        existingContact.setId(contactId);
        Client client = new Client();
        client.setId(1L);
        existingContact.setClient(client);

        when(contactRepository.findById(contactId)).thenReturn(Optional.of(existingContact));

        Long result = contactService.deleteContact(contactId);

        assertEquals(contactId, result);
        verify(contactRepository).deleteById(contactId);
        verify(interactionService).createInteraction(any());
    }

    @Test
    void deleteContact_whenNotExists_shouldThrowContactNotFoundException() {
        Long contactId = 1L;

        when(contactRepository.findById(contactId)).thenReturn(Optional.empty());

        CRMBaseException exception = assertThrows(CRMBaseException.class, () -> {
            contactService.deleteContact(contactId);
        });
        assertEquals("Contact with id: " + contactId + " not found", exception.getMessage());
    }
}