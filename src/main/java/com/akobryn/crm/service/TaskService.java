package com.akobryn.crm.service;

import com.akobryn.crm.constants.TaskStatus;
import com.akobryn.crm.dto.TaskDTO;

import java.util.List;

public interface TaskService {
    List<TaskDTO> getAllTasks();

    TaskDTO createTask(TaskDTO taskDTO);

    TaskDTO updateTaskStatus(Long taskId, TaskStatus taskStatus);

    TaskDTO setContactId(Long taskId, Long contactId);

    Long deleteTask(Long taskId);

    List<TaskDTO> getTasksByClientId(Long clientId);

    List<TaskDTO> getTasksByContactId(Long contactId);

    TaskDTO findTaskById(Long taskId);


}
