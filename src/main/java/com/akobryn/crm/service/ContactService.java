package com.akobryn.crm.service;

import com.akobryn.crm.dto.ContactDTO;

import java.util.List;

public interface ContactService {

    List<ContactDTO> getAllContacts();

    ContactDTO getContactById(Long contactId);

    ContactDTO createContact(ContactDTO contactDTO);

    ContactDTO updateContact(Long contactId, ContactDTO contactDTO);

    Long deleteContact(Long contactId);

    List<ContactDTO> getContactsByClientId(Long clientId);

    List<ContactDTO> searchContactsByClientId(Long clientId, String firstName, String lastName, String email);
}
