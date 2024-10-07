package com.akobryn.crm.dto;

import com.akobryn.crm.constants.InteractionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InteractionDTO {
    private Long id;
    private Long clientId;
    private Long contactId;
    private InteractionType type;
    private LocalDateTime date;
    private String message;
}
