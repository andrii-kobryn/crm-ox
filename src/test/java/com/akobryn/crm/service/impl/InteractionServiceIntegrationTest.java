package com.akobryn.crm.service.impl;

import com.akobryn.crm.dto.InteractionDTO;
import com.akobryn.crm.entity.Interaction;
import com.akobryn.crm.mapper.InteractionMapper;
import com.akobryn.crm.repository.InteractionRepository;
import com.akobryn.crm.service.InteractionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class InteractionServiceIntegrationTest {
    @Autowired
    private InteractionService interactionService;

    @Autowired
    private InteractionRepository interactionRepository;

    @Autowired
    private InteractionMapper interactionMapper;

    @BeforeEach
    void setUp() {
        interactionRepository.deleteAll(); 
    }

    @Test
    void createInteraction_shouldSaveAndReturnInteractionDTO() {
        
        InteractionDTO interactionDTO = new InteractionDTO();
        interactionDTO.setId(1L);
        interactionDTO.setClientId(1L);
        interactionDTO.setContactId(1L);
        interactionDTO.setMessage("Some value"); 

        
        InteractionDTO result = interactionService.createInteraction(interactionDTO);

        
        Interaction savedInteraction = interactionRepository.findById(result.getId()).orElse(null);
        assertEquals(interactionDTO.getId(), result.getId());
        assertEquals(interactionDTO.getMessage(), savedInteraction.getMessage()); 
    }

    @Test
    void getInteractionsByClientId_shouldReturnInteractionDTOList() {
        
        Interaction interaction = new Interaction();
        interaction.setClientId(1L);
        interaction.setContactId(100L);
        interactionRepository.save(interaction); 

        
        List<InteractionDTO> result = interactionService.getInteractionsByClientId(1L);

        
        assertEquals(1, result.size());
        assertEquals(interaction.getId(), result.get(0).getId());
    }

    @Test
    void getInteractionsByContactIdAndClientId_shouldReturnInteractionDTOList() {
        
        Interaction interaction = new Interaction();
        interaction.setClientId(1L);
        interaction.setContactId(1L); 
        interactionRepository.save(interaction);

        
        List<InteractionDTO> result = interactionService.getInteractionsByContactIdAndClientId(1L, 1L);

        
        assertEquals(1, result.size());
        assertEquals(interaction.getId(), result.get(0).getId());
    }
}