package com.akobryn.crm.service.impl;

import com.akobryn.crm.dto.ClientDTO;
import com.akobryn.crm.entity.Client;
import com.akobryn.crm.exceptions.CRMExceptions;
import com.akobryn.crm.mapper.ClientMapper;
import com.akobryn.crm.repository.ClientRepository;
import com.akobryn.crm.repository.ContactRepository;
import com.akobryn.crm.service.InteractionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceUnitTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private InteractionService interactionService;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client client;
    private ClientDTO clientDTO;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
        client.setCompanyName("Test Company");
        client.setAddress("123 Test St");

        clientDTO = new ClientDTO();
        clientDTO.setId(1L);
        clientDTO.setCompanyName("Test Company");
        clientDTO.setAddress("123 Test St");
    }

    @Test
    void getAllClients_shouldReturnListOfClients() {
        when(clientRepository.findAll()).thenReturn(List.of(client));
        when(clientMapper.toDTO(any(Client.class))).thenReturn(clientDTO);

        List<ClientDTO> result = clientService.getAllClients();

        assertEquals(1, result.size());
        assertEquals(clientDTO, result.get(0));
        verify(clientRepository).findAll();
    }

    @Test
    void getClientById_shouldReturnClientDTO() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientMapper.toDTO(client)).thenReturn(clientDTO);

        ClientDTO result = clientService.getClientById(1L);

        assertEquals(clientDTO, result);
        verify(clientRepository).findById(1L);
    }

    @Test
    void getClientById_shouldThrowException_whenClientNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CRMExceptions.clientNotFound(1L).getClass(), () -> clientService.getClientById(1L));
        verify(clientRepository).findById(1L);
    }

    @Test
    void createClient_shouldReturnCreatedClientDTO() {
        when(clientMapper.toEntity(clientDTO)).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(client);
        when(clientMapper.toDTO(client)).thenReturn(clientDTO);

        ClientDTO result = clientService.createClient(clientDTO);

        assertEquals(clientDTO, result);
        verify(clientRepository).save(client);
        verify(interactionService).createInteraction(any());
    }

    @Test
    void updateClient_shouldReturnUpdatedClientDTO() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientMapper.toDTO(client)).thenReturn(clientDTO);
        when(clientRepository.save(client)).thenReturn(client);

        ClientDTO result = clientService.updateClient(1L, clientDTO);

        assertEquals(clientDTO, result);
        verify(clientRepository).save(client);
    }

    @Test
    void updateClient_shouldThrowException_whenClientNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CRMExceptions.clientNotFound(1L).getClass(), () -> clientService.updateClient(1L, clientDTO));
        verify(clientRepository).findById(1L);
    }

    @Test
    void deleteClient_shouldReturnClientId() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        Long result = clientService.deleteClient(1L);

        assertEquals(1L, result);
        verify(contactRepository).deleteAllByClientId(1L);
        verify(clientRepository).deleteById(1L);
    }

    @Test
    void deleteClient_shouldThrowException_whenClientNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CRMExceptions.clientNotFound(1L).getClass(), () -> clientService.deleteClient(1L));
        verify(clientRepository).findById(1L);
    }

    @Test
    void searchClients_shouldReturnFilteredClients() {
        when(clientRepository.findAll()).thenReturn(List.of(client));
        when(clientMapper.toDTO(any(Client.class))).thenReturn(clientDTO);

        List<ClientDTO> result = clientService.searchClients("Test Company", null, null);

        assertEquals(1, result.size());
        assertEquals(clientDTO, result.get(0));
    }
}
