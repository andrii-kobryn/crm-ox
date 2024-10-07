package com.akobryn.crm.service.impl;

import com.akobryn.crm.constants.NotificationConstants;
import com.akobryn.crm.constants.TaskStatus;
import com.akobryn.crm.dto.TaskDTO;
import com.akobryn.crm.entity.Contact;
import com.akobryn.crm.entity.Task;
import com.akobryn.crm.exceptions.CRMExceptions;
import com.akobryn.crm.mapper.TaskMapper;
import com.akobryn.crm.repository.ContactRepository;
import com.akobryn.crm.repository.TaskRepository;
import com.akobryn.crm.service.NotificationService;
import com.akobryn.crm.service.TaskService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskServiceImpl implements TaskService {
    TaskRepository taskRepository;
    ContactRepository contactRepository;
    TaskMapper taskMapper;
    NotificationService notificationService;


    @Cacheable(value = "tasks")
    @Override
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toDTO)
                .toList();
    }

    /**
     * Creates task
     *
     * @param taskDTO
     * @return TaskDTO
     */
    @Override
    @Transactional
    public TaskDTO createTask(TaskDTO taskDTO) {
        Task taskToSave = taskMapper.toEntity(taskDTO);
        Task savedTask = taskRepository.save(taskToSave);
        return taskMapper.toDTO(savedTask);
    }

    /**
     * Updates task status
     * @param taskId
     * @param taskStatus possible task statuses
     * @return TaskDTO
     */
    @CachePut(value = "tasks", key = "#taskId")
    @Override
    @Transactional
    public TaskDTO updateTaskStatus(Long taskId, TaskStatus taskStatus) {
        return taskRepository.findById(taskId)
                .map(task -> {
                    task.setStatus(taskStatus);
                    Task savedTask = taskRepository.save(task);
                    notificationService.sendTaskStatusNotification(taskId,
                            String.format(NotificationConstants.CHANGED_TASK_STATUS, taskId, taskStatus.getDisplayName()));
                    return taskMapper.toDTO(savedTask);
                })
                .orElseThrow(() -> CRMExceptions.taskNotFound(taskId));
    }

    /**
     * Updates task contact id
     * @param taskId
     * @param contactId
     * @return TaskDTO
     */
    @CachePut(value = "tasks", key = "#taskId")
    @Override
    @Transactional
    public TaskDTO setContactId(Long taskId, Long contactId) {
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> CRMExceptions.contactNotFound(contactId));
        return taskRepository.findById(taskId)
                .map(task -> {
                    task.setContact(contact);
                    Task savedTask = taskRepository.save(task);
                    return taskMapper.toDTO(savedTask);
                })
                .orElseThrow(() -> CRMExceptions.taskNotFound(taskId));
    }

    /**
     * Deletes task
     * @param taskId
     * @return TaskDTO
     */
    @CacheEvict(value = "tasks", key = "#taskId")
    @Override
    @Transactional
    public Long deleteTask(Long taskId) {
        return taskRepository.findById(taskId)
                .map(task -> {
                    taskRepository.deleteById(taskId);
                    return taskId;
                })
                .orElseThrow(() -> CRMExceptions.taskNotFound(taskId));
    }

    @Override
    public List<TaskDTO> getTasksByClientId(Long clientId) {
        return taskRepository.findByContactClientId(clientId).stream()
                .map(taskMapper::toDTO)
                .toList();
    }

    @Override
    public List<TaskDTO> getTasksByContactId(Long contactId) {
        return taskRepository.findByContactId(contactId).stream()
                .map(taskMapper::toDTO)
                .toList();
    }

    @Cacheable(value = "tasks", key = "#taskId")
    @Override
    public TaskDTO findTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .map(taskMapper::toDTO)
                .orElseThrow(() -> CRMExceptions.taskNotFound(taskId));
    }
}
