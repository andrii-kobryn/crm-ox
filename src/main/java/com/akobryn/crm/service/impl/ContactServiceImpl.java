package com.akobryn.crm.service.impl;

import com.akobryn.crm.constants.InteractionMessages;
import com.akobryn.crm.constants.InteractionType;
import com.akobryn.crm.dto.ContactDTO;
import com.akobryn.crm.dto.InteractionDTO;
import com.akobryn.crm.entity.Contact;
import com.akobryn.crm.exceptions.CRMExceptions;
import com.akobryn.crm.mapper.ContactMapper;
import com.akobryn.crm.repository.ContactRepository;
import com.akobryn.crm.service.ContactService;
import com.akobryn.crm.service.InteractionService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContactServiceImpl implements ContactService {
    ContactRepository contactRepository;
    ContactMapper contactMapper;
    InteractionService interactionService;

    @Cacheable(value = "contacts")
    @Override
    public List<ContactDTO> getAllContacts() {
        return contactRepository.findAll().stream()
                .map(contactMapper::toDTO)
                .toList();
    }

    /**
     * Find contact by id and maps to ClientDto
     * @param contactId
     * @return ContactDTO
     */
    @Cacheable(value = "contacts", key = "#contactId")
    @Override
    public ContactDTO getContactById(Long contactId) {
        return contactRepository.findById(contactId)
                .map(contactMapper::toDTO)
                .orElseThrow(() -> CRMExceptions.contactNotFound(contactId));
    }

    /**
     * Creates contact and interaction
     *
     * @param contactDTO
     * @return ContactDTO
     */
    @Override
    @Transactional
    public ContactDTO createContact(ContactDTO contactDTO) {
        Contact contactToSave = contactMapper.toEntity(contactDTO);
        Contact savedContact = contactRepository.save(contactToSave);
        Long contactId = savedContact.getId();
        Long clientId = savedContact.getClient().getId();
        InteractionDTO interactionDTO = InteractionDTO.builder()
                .clientId(clientId)
                .contactId(contactId)
                .type(InteractionType.ADDING)
                .date(LocalDateTime.now())
                .message(String.format(InteractionMessages.CONTACT_WAS_CREATED, contactId, clientId))
                .build();
        interactionService.createInteraction(interactionDTO);
        return contactMapper.toDTO(savedContact);
    }

    /**
     * Updates contact and creates interaction
     * @param contactId
     * @param contactDTO
     * @return ContactDTO
     */
    @CachePut(value = "contacts", key = "#contactDTO.id")
    @Override
    @Transactional
    public ContactDTO updateContact(Long contactId, ContactDTO contactDTO) {
        return contactRepository.findById(contactId)
                .map(contact -> {
                    contactMapper.updateContactFromDTO(contactDTO, contact);
                    Contact updatedContact = contactRepository.save(contact);
                    Long clientId = updatedContact.getClient().getId();
                    InteractionDTO interactionDTO = InteractionDTO.builder()
                            .clientId(clientId)
                            .contactId(contactId)
                            .type(InteractionType.UPDATING)
                            .date(LocalDateTime.now())
                            .message(String.format(InteractionMessages.CONTACT_WAS_UPDATED, contactId, clientId))
                            .build();
                    interactionService.createInteraction(interactionDTO);
                    return contactMapper.toDTO(updatedContact);
                })
                .orElseThrow(() -> CRMExceptions.contactNotFound(contactId));
    }

    /**
     * Deletes contact and creates interaction
     * @param contactId
     * @return ContactDTO
     */
    @CacheEvict(value = "contacts", key = "#contactId")
    @Override
    @Transactional
    public Long deleteContact(Long contactId) {
        return contactRepository.findById(contactId)
                .map(contact -> {
                    contactRepository.deleteById(contactId);
                    Long clientId = contact.getClient().getId();
                    InteractionDTO interactionDTO = InteractionDTO.builder()
                            .clientId(clientId)
                            .contactId(contactId)
                            .type(InteractionType.DELETING)
                            .date(LocalDateTime.now())
                            .message(String.format(InteractionMessages.CONTACT_WAS_DELETED, contactId, clientId))
                            .build();
                    interactionService.createInteraction(interactionDTO);
                    return contactId;
                })
                .orElseThrow(() -> CRMExceptions.contactNotFound(contactId));
    }

    @Override
    public List<ContactDTO> getContactsByClientId(Long clientId) {
        return contactRepository.findContactsByClientId(clientId).stream()
                .map(contactMapper::toDTO)
                .toList();
    }

    /**
     * Searchs for contacts based on the following parameters:
     * @param firstName
     * @param lastName
     * @param email
     * @return ContactDto
     */
    @Override
    public List<ContactDTO> searchContactsByClientId(Long clientId, String firstName, String lastName, String email) {
        return contactRepository.findContactsByClientId(clientId).stream()
                .filter(contact -> (firstName == null || firstName.isEmpty() || contact.getFirstName().contains(firstName)))
                .filter(contact -> (lastName == null || lastName.isEmpty() || contact.getLastName().contains(lastName)))
                .filter(contact -> (email == null || email.isEmpty() || contact.getEmail().contains(email)))
                .map(contactMapper::toDTO)
                .toList();
    }
}

