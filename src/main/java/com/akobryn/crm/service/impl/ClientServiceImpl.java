package com.akobryn.crm.service.impl;

import com.akobryn.crm.constants.InteractionMessages;
import com.akobryn.crm.constants.InteractionType;
import com.akobryn.crm.dto.ClientDTO;
import com.akobryn.crm.dto.InteractionDTO;
import com.akobryn.crm.entity.Client;
import com.akobryn.crm.exceptions.CRMExceptions;
import com.akobryn.crm.mapper.ClientMapper;
import com.akobryn.crm.repository.ClientRepository;
import com.akobryn.crm.repository.ContactRepository;
import com.akobryn.crm.service.ClientService;
import com.akobryn.crm.service.InteractionService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClientServiceImpl implements ClientService {
    ClientRepository clientRepository;
    ClientMapper clientMapper;
    ContactRepository contactRepository;
    InteractionService interactionService;

    @Override
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll().stream()
                .map(clientMapper::toDTO)
                .toList();
    }

    /**
     * Find client by id and maps to ClientDto
     * @param clientId
     * @return ClientDTO
     */
    @Override
    public ClientDTO getClientById(Long clientId) {
        return clientRepository.findById(clientId)
                .map(clientMapper::toDTO)
                .orElseThrow(() -> CRMExceptions.clientNotFound(clientId));
    }

    /**
     * Creates client and interaction
     *
     * @param clientDTO
     * @return ClientDTO
     */
    @Override
    @Transactional
    public ClientDTO createClient(ClientDTO clientDTO) {
        Client clientToSave = clientMapper.toEntity(clientDTO);
        Client savedClient = clientRepository.save(clientToSave);
        InteractionDTO interactionDTO = InteractionDTO.builder()
                .clientId(savedClient.getId())
                .type(InteractionType.ADDING)
                .date(LocalDateTime.now())
                .message(String.format(InteractionMessages.CLIENT_WAS_CREATED, savedClient.getId()))
                .build();
        interactionService.createInteraction(interactionDTO);
        return clientMapper.toDTO(savedClient);
    }

    /**
     * Updates client and creates interaction
     * @param clientId
     * @param clientDTO
     * @return ClientDTO
     */
    @Override
    @Transactional
    public ClientDTO updateClient(Long clientId, ClientDTO clientDTO) {
        return clientRepository.findById(clientId)
                .map(client -> {
                    clientMapper.updateClientFromDTO(clientDTO, client);
                    Client updatedClient = clientRepository.save(client);
                    InteractionDTO interactionDTO = InteractionDTO.builder()
                            .clientId(clientId)
                            .type(InteractionType.UPDATING)
                            .date(LocalDateTime.now())
                            .message(String.format(InteractionMessages.CLIENT_WAS_UPDATED, clientId))
                            .build();
                    interactionService.createInteraction(interactionDTO);
                    return clientMapper.toDTO(updatedClient);
                })
                .orElseThrow(() -> CRMExceptions.clientNotFound(clientId));
    }

    /**
     * Deletes client and creates interaction
     * @param clientId
     * @return ClientDTO
     */
    @Override
    @Transactional
    public Long deleteClient(Long clientId) {
        return clientRepository.findById(clientId)
                .map(client -> {
                    contactRepository.deleteAllByClientId(clientId);
                    clientRepository.deleteById(clientId);
                    return clientId;
                })
                .orElseThrow(() -> CRMExceptions.clientNotFound(clientId));
    }

    /**
     * Searchs for clients based on the following parameters:
     * @param companyName
     * @param industry
     * @param address
     * @return ClientDto
     */
    @Override
    public List<ClientDTO> searchClients(String companyName, String industry, String address) {
        return clientRepository.findAll().stream()
                .filter(client -> (address == null || address.isEmpty() || client.getAddress().contains(address)))
                .filter(client -> (companyName == null || companyName.isEmpty() || client.getCompanyName().contains(companyName)))
                .filter(client -> (industry == null || industry.isEmpty() || client.getIndustry().contains(industry)))
                .map(clientMapper::toDTO)
                .toList();
    }
}
