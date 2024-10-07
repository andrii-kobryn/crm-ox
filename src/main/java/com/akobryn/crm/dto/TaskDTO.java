package com.akobryn.crm.dto;

import com.akobryn.crm.constants.TaskStatus;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class TaskDTO implements Serializable {
    private static final long SERIAL_VERSION_UID = 2L;

    private Long id;
    private String description;
    private TaskStatus status;
    private LocalDate dueDate;
    private Long contactId;
}
