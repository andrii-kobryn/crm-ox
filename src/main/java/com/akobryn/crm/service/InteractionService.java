package com.akobryn.crm.service;

import com.akobryn.crm.dto.InteractionDTO;

import java.util.List;

public interface InteractionService {
    InteractionDTO createInteraction(InteractionDTO interactionDTO);
    List<InteractionDTO> getInteractionsByClientId(Long clientId);
    List<InteractionDTO> getInteractionsByContactIdAndClientId(Long contactId, Long clientId);
}
