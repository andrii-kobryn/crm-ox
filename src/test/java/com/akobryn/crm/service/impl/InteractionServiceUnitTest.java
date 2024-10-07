package com.akobryn.crm.service.impl;

import com.akobryn.crm.dto.InteractionDTO;
import com.akobryn.crm.entity.Interaction;
import com.akobryn.crm.mapper.InteractionMapper;
import com.akobryn.crm.repository.InteractionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InteractionServiceUnitTest {
    @Mock
    private InteractionRepository interactionRepository;

    @Mock
    private InteractionMapper interactionMapper;

    @InjectMocks
    private InteractionServiceImpl interactionService;

    private Interaction interaction;
    private InteractionDTO interactionDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        
        interaction = new Interaction();
        interaction.setId(1L);
        interactionDTO = new InteractionDTO();
        interactionDTO.setId(1L);
    }

    @Test
    void createInteraction_shouldReturnSavedInteractionDTO() {
        when(interactionMapper.toEntity(interactionDTO)).thenReturn(interaction);
        when(interactionRepository.save(interaction)).thenReturn(interaction);
        when(interactionMapper.toDto(interaction)).thenReturn(interactionDTO);

        InteractionDTO result = interactionService.createInteraction(interactionDTO);

        verify(interactionMapper).toEntity(interactionDTO);
        verify(interactionRepository).save(interaction);
        verify(interactionMapper).toDto(interaction);
        assertEquals(interactionDTO, result);
    }

    @Test
    void getInteractionsByClientId_shouldReturnInteractionDTOList() {
        when(interactionRepository.findByClientId(1L)).thenReturn(Collections.singletonList(interaction));
        when(interactionMapper.toDto(interaction)).thenReturn(interactionDTO);

        List<InteractionDTO> result = interactionService.getInteractionsByClientId(1L);

        verify(interactionRepository).findByClientId(1L);
        verify(interactionMapper).toDto(interaction);
        assertEquals(1, result.size());
        assertEquals(interactionDTO, result.get(0));
    }

    @Test
    void getInteractionsByContactIdAndClientId_shouldReturnInteractionDTOList() {
        when(interactionRepository.findByContactIdAndClientId(1L, 1L)).thenReturn(Collections.singletonList(interaction));
        when(interactionMapper.toDto(interaction)).thenReturn(interactionDTO);

        List<InteractionDTO> result = interactionService.getInteractionsByContactIdAndClientId(1L, 1L);

        verify(interactionRepository).findByContactIdAndClientId(1L, 1L);
        verify(interactionMapper).toDto(interaction);
        assertEquals(1, result.size());
        assertEquals(interactionDTO, result.get(0));
    }
}