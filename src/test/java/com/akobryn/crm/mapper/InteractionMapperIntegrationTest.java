package com.akobryn.crm.mapper;

import com.akobryn.crm.constants.InteractionType;
import com.akobryn.crm.dto.InteractionDTO;
import com.akobryn.crm.entity.Interaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InteractionMapperIntegrationTest {

    @Autowired
    private InteractionMapper interactionMapper;

    @Test
    public void testToDto() {
        Interaction interaction = new Interaction();
        interaction.setId(1L);
        interaction.setClientId(101L);
        interaction.setContactId(202L);
        interaction.setType(InteractionType.ADDING);
        interaction.setDate(LocalDateTime.now());

        InteractionDTO interactionDTO = interactionMapper.toDto(interaction);

        assertEquals(interaction.getClientId(), interactionDTO.getClientId());
        assertEquals(interaction.getContactId(), interactionDTO.getContactId());
        assertEquals(interaction.getType(), interactionDTO.getType());
        assertEquals(interaction.getDate(), interactionDTO.getDate());
    }

    @Test
    public void testToEntity() {
        InteractionDTO interactionDTO = new InteractionDTO();
        interactionDTO.setClientId(101L);
        interactionDTO.setContactId(202L);
        interactionDTO.setType(InteractionType.DELETING);
        interactionDTO.setDate(LocalDateTime.now());

        Interaction interaction = interactionMapper.toEntity(interactionDTO);

        assertEquals(interactionDTO.getClientId(), interaction.getClientId());
        assertEquals(interactionDTO.getContactId(), interaction.getContactId());
        assertEquals(interactionDTO.getType(), interaction.getType());
        assertEquals(interactionDTO.getDate(), interaction.getDate());
    }
}