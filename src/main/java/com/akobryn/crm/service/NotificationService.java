package com.akobryn.crm.service;

public interface NotificationService {

    void sendTaskStatusNotification(Long taskId, String message);
}
