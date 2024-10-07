package com.akobryn.crm.service.impl;

import com.akobryn.crm.constants.TaskStatus;
import com.akobryn.crm.dto.TaskDTO;
import com.akobryn.crm.entity.Contact;
import com.akobryn.crm.entity.Task;
import com.akobryn.crm.exceptions.CRMExceptions;
import com.akobryn.crm.mapper.TaskMapper;
import com.akobryn.crm.repository.ContactRepository;
import com.akobryn.crm.repository.TaskRepository;
import com.akobryn.crm.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskServiceUnitTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private NotificationService notificationService;

    private Task testTask;
    private TaskDTO testTaskDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        testTask = new Task();
        testTask.setId(1L);
        testTask.setDescription("Test Task");
        testTask.setStatus(TaskStatus.OPEN);

        testTaskDTO = new TaskDTO();
        testTaskDTO.setId(1L);
        testTaskDTO.setDescription("Test Task DTO");
        testTaskDTO.setStatus(TaskStatus.OPEN);
    }

    @Test
    public void testGetAllTasks() {
        
        when(taskRepository.findAll()).thenReturn(List.of(testTask));
        when(taskMapper.toDTO(testTask)).thenReturn(testTaskDTO);

        
        List<TaskDTO> result = taskService.getAllTasks();

        
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDescription()).isEqualTo("Test Task DTO");
        verify(taskRepository).findAll();
        verify(taskMapper).toDTO(testTask);
    }

    @Test
    public void testCreateTask() {
        
        when(taskMapper.toEntity(testTaskDTO)).thenReturn(testTask);
        when(taskRepository.save(testTask)).thenReturn(testTask);
        when(taskMapper.toDTO(testTask)).thenReturn(testTaskDTO);

        
        TaskDTO result = taskService.createTask(testTaskDTO);

        
        assertThat(result).isEqualTo(testTaskDTO);
        verify(taskMapper).toEntity(testTaskDTO);
        verify(taskRepository).save(testTask);
        verify(taskMapper).toDTO(testTask);
    }

    @Test
    public void testUpdateTaskStatus_Success() {
        
        testTask.setStatus(TaskStatus.COMPLETED);
        testTaskDTO.setStatus(TaskStatus.COMPLETED);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(testTask)).thenReturn(testTask);
        when(taskMapper.toDTO(testTask)).thenReturn(testTaskDTO);

        
        TaskDTO result = taskService.updateTaskStatus(1L, TaskStatus.COMPLETED);

        
        assertThat(result.getStatus()).isEqualTo(TaskStatus.COMPLETED);
        verify(taskRepository).findById(1L);
        verify(notificationService).sendTaskStatusNotification(1L, "Статус завдання з id 1 було змінено на Completed");
    }

    @Test
    public void testUpdateTaskStatus_TaskNotFound() {
        
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        
        assertThatThrownBy(() -> taskService.updateTaskStatus(1L, TaskStatus.COMPLETED))
                .isInstanceOf(CRMExceptions.taskNotFound(1L).getClass())
                .hasMessageContaining("Task with id: 1 not found");
    }

    @Test
    public void testSetContactId_Success() {
        
        Contact testContact = new Contact();
        testContact.setId(2L);
        when(contactRepository.findById(2L)).thenReturn(Optional.of(testContact));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(testTask)).thenReturn(testTask);
        when(taskMapper.toDTO(testTask)).thenReturn(testTaskDTO);

        
        TaskDTO result = taskService.setContactId(1L, 2L);

        
        assertThat(result.getId()).isEqualTo(testTaskDTO.getId());
        assertThat(result.getDescription()).isEqualTo(testTaskDTO.getDescription());
        verify(contactRepository).findById(2L);
        verify(taskRepository).findById(1L);
    }

    @Test
    public void testSetContactId_TaskNotFound() {
        
        when(contactRepository.findById(2L)).thenReturn(Optional.of(new Contact()));
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        
        assertThatThrownBy(() -> taskService.setContactId(1L, 2L))
                .isInstanceOf(CRMExceptions.taskNotFound(1L).getClass())
                .hasMessageContaining("Task with id: 1 not found");
    }

    @Test
    public void testDeleteTask_Success() {
        
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        
        Long result = taskService.deleteTask(1L);

        
        assertThat(result).isEqualTo(1L);
        verify(taskRepository).deleteById(1L);
    }

    @Test
    public void testDeleteTask_TaskNotFound() {
        
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        
        assertThatThrownBy(() -> taskService.deleteTask(1L))
                .isInstanceOf(CRMExceptions.taskNotFound(1L).getClass())
                .hasMessageContaining("Task with id: 1 not found");
    }

    @Test
    public void testFindTaskById_Success() {
        
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskMapper.toDTO(testTask)).thenReturn(testTaskDTO);

        
        TaskDTO result = taskService.findTaskById(1L);

        
        assertThat(result).isEqualTo(testTaskDTO);
        verify(taskRepository).findById(1L);
        verify(taskMapper).toDTO(testTask);
    }

    @Test
    public void testFindTaskById_TaskNotFound() {
        
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        
        assertThatThrownBy(() -> taskService.findTaskById(1L))
                .isInstanceOf(CRMExceptions.taskNotFound(1L).getClass())
                .hasMessageContaining("Task with id: 1 not found");
    }
}