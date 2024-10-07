package com.akobryn.crm.service;

import com.akobryn.crm.dto.ClientDTO;

import java.util.List;

public interface ClientService {

    List<ClientDTO> getAllClients();

    ClientDTO getClientById(Long clientId);

    ClientDTO createClient(ClientDTO clientDTO);

    ClientDTO updateClient(Long clientId, ClientDTO clientDTO);

    Long deleteClient(Long id);

    List<ClientDTO> searchClients(String companyName, String industry, String address);
}
