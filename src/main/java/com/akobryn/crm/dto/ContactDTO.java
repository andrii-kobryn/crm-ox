package com.akobryn.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactDTO implements Serializable {
    private static final long SERIAL_VERSION_UID = 1L;

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Long clientId;
    private Set<TaskDTO> tasks = new HashSet<>();
}
