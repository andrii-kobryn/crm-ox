package com.akobryn.crm.service.impl;

import com.akobryn.crm.dto.notification.TaskStatusNotification;
import com.akobryn.crm.entity.Client;
import com.akobryn.crm.entity.Contact;
import com.akobryn.crm.entity.Task;
import com.akobryn.crm.mapper.TaskMapper;
import com.akobryn.crm.repository.ClientRepository;
import com.akobryn.crm.repository.ContactRepository;
import com.akobryn.crm.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") 
@Transactional
class NotificationServiceIntegrationTest {
    @Autowired
    private NotificationServiceImpl notificationService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private TaskMapper taskMapper;

    @MockBean 
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        
        taskRepository.deleteAll();
    }

    @Test
    void sendTaskStatusNotification_shouldSendMessage() throws Exception {
        
        Long taskId = 1L;
        String message = "Test message";
        TaskStatusNotification notification = new TaskStatusNotification(message, taskId);
        String jsonMessage = objectMapper.writeValueAsString(notification);

        
        notificationService.sendTaskStatusNotification(taskId, message);

        
        
        verify(messagingTemplate).convertAndSend("/topic/tasks", jsonMessage);
    }

    @Test
    void sendTaskDeadlineNotifications_shouldSendDeadlineMessages() {
        
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Client client = new Client();
        clientRepository.save(client);

        Contact contact = new Contact();
        contact.setClient(client);
        contactRepository.save(contact);

        
        Task task1 = new Task();
        task1.setId(1L);
        task1.setDueDate(tomorrow);
        task1.setContact(contact);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setDueDate(tomorrow);
        task2.setContact(contact);

        taskRepository.saveAll(List.of(task1, task2));

        
        notificationService.sendTaskDeadlineNotifications();

        
        verify(messagingTemplate).convertAndSend("/topic/deadlines", "Термін виконання завдання #1 настає завтра!");
        verify(messagingTemplate).convertAndSend("/topic/deadlines", "Термін виконання завдання #2 настає завтра!");
    }

    @Test
    void sendTaskDeadlineNotifications_shouldHandleEmptyTasksList() {
        
        notificationService.sendTaskDeadlineNotifications();

        
        verify(messagingTemplate, never()).convertAndSend(anyString(), anyString());
    }
}