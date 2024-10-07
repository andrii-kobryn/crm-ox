package com.akobryn.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {

    private Long id;
    private String companyName;
    private String industry;
    private String address;
    private Set<ContactDTO> contacts = new HashSet<>();
}
