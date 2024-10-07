package com.akobryn.crm.service.impl;

import com.akobryn.crm.dto.TaskDTO;
import com.akobryn.crm.dto.notification.TaskStatusNotification;
import com.akobryn.crm.entity.Task;
import com.akobryn.crm.mapper.TaskMapper;
import com.akobryn.crm.repository.TaskRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.Mockito.*;

class NotificationServiceUnitTest {
    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendTaskStatusNotification_shouldSendMessage() throws JsonProcessingException {
        
        Long taskId = 1L;
        String message = "Test message";
        TaskStatusNotification notification = new TaskStatusNotification(message, taskId);
        String jsonMessage = "{\"message\":\"Test message\",\"taskId\":1}";

        when(objectMapper.writeValueAsString(notification)).thenReturn(jsonMessage);

        
        notificationService.sendTaskStatusNotification(taskId, message);

        
        verify(objectMapper).writeValueAsString(notification);
        verify(messagingTemplate).convertAndSend("/topic/tasks", jsonMessage);
    }

    @Test
    void sendTaskDeadlineNotifications_shouldSendDeadlineMessages() {
        
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Task task1 = new Task();
        task1.setId(1L);
        Task task2 = new Task();
        task2.setId(2L);

        TaskDTO taskDTO1 = new TaskDTO();
        taskDTO1.setId(1L);
        TaskDTO taskDTO2 = new TaskDTO();
        taskDTO2.setId(2L);

        when(taskRepository.findTasksDueTomorrow(tomorrow)).thenReturn(Arrays.asList(task1, task2));
        when(taskMapper.toDTO(task1)).thenReturn(taskDTO1);
        when(taskMapper.toDTO(task2)).thenReturn(taskDTO2);

        
        notificationService.sendTaskDeadlineNotifications();

        
        verify(messagingTemplate).convertAndSend("/topic/deadlines", "Термін виконання завдання #1 настає завтра!");
        verify(messagingTemplate).convertAndSend("/topic/deadlines", "Термін виконання завдання #2 настає завтра!");
    }

    @Test
    void sendTaskDeadlineNotifications_shouldHandleEmptyTasksList() {
        
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        when(taskRepository.findTasksDueTomorrow(tomorrow)).thenReturn(Arrays.asList());

        
        notificationService.sendTaskDeadlineNotifications();

        
        verify(messagingTemplate, never()).convertAndSend(anyString(), anyString());
    }
}