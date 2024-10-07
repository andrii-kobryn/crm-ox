package com.akobryn.crm.service.impl;

import com.akobryn.crm.dto.InteractionDTO;
import com.akobryn.crm.entity.Interaction;
import com.akobryn.crm.mapper.InteractionMapper;
import com.akobryn.crm.repository.InteractionRepository;
import com.akobryn.crm.service.InteractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InteractionServiceImpl implements InteractionService {

    private final InteractionRepository interactionRepository;

    private final InteractionMapper interactionMapper;

    @Override
    public InteractionDTO createInteraction(InteractionDTO interactionDTO) {
        Interaction interactionToSave = interactionMapper.toEntity(interactionDTO);
        Interaction savedInteraction = interactionRepository.save(interactionToSave);
        return interactionMapper.toDto(savedInteraction);
    }

    @Override
    public List<InteractionDTO> getInteractionsByClientId(Long clientId) {
        return interactionRepository.findByClientId(clientId).stream()
                .map(interactionMapper::toDto)
                .toList();
    }

    @Override
    public List<InteractionDTO> getInteractionsByContactIdAndClientId(Long contactId, Long clientId) {
        return interactionRepository.findByContactIdAndClientId(contactId, clientId).stream()
                .map(interactionMapper::toDto)
                .toList();
    }
}
