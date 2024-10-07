package com.akobryn.crm.service.impl;

import com.akobryn.crm.dto.TaskDTO;
import com.akobryn.crm.dto.notification.TaskStatusNotification;
import com.akobryn.crm.mapper.TaskMapper;
import com.akobryn.crm.repository.TaskRepository;
import com.akobryn.crm.service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;
    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;

    public void sendTaskStatusNotification(Long taskId, String message) {
        TaskStatusNotification taskStatusNotification = new TaskStatusNotification(message, taskId);
        try {
            String jsonMessage = objectMapper.writeValueAsString(taskStatusNotification);
            messagingTemplate.convertAndSend("/topic/tasks", jsonMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate = 3600000)
    public void sendTaskDeadlineNotifications() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<TaskDTO> tasksDueTomorrow = taskRepository.findTasksDueTomorrow(tomorrow).stream()
                .map(taskMapper::toDTO)
                .toList();

        for (TaskDTO task : tasksDueTomorrow) {
            String message = "Термін виконання завдання #" + task.getId() + " настає завтра!";
            messagingTemplate.convertAndSend("/topic/deadlines", message);
        }
    }


}
