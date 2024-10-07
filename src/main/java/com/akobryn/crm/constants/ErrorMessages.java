package com.akobryn.crm.constants;

import lombok.Getter;

@Getter
public class ErrorMessages {
    public static final String CLIENT_NOT_FOUND_MESSAGE = "Client with id: %s not found";
    public static final String TASK_NOT_FOUND_MESSAGE = "Task with id: %s not found";
    public static final String CONTACT_NOT_FOUND_MESSAGE = "Contact with id: %s not found";
    public static final String USER_NOT_FOUND_BY_USERNAME_MESSAGE = "User with username: %s not found";
    public static final String USER_ALREADY_EXISTS_MESSAGE = "User with username: %s already exists";
}
