package com.akobryn.crm.exceptions;

import com.akobryn.crm.constants.ErrorMessages;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

@UtilityClass
public class CRMExceptions {

    public CRMBaseException clientNotFound(Long clientId) {
        String message = String.format(ErrorMessages.CLIENT_NOT_FOUND_MESSAGE,  clientId);
        return new CRMBaseException(message, HttpStatus.NOT_FOUND);
    }

    public CRMBaseException contactNotFound(Long contactId) {
        String message = String.format(ErrorMessages.CONTACT_NOT_FOUND_MESSAGE,  contactId);
        return new CRMBaseException(message, HttpStatus.NOT_FOUND);
    }

    public CRMBaseException taskNotFound(Long taskId) {
        String message = String.format(ErrorMessages.TASK_NOT_FOUND_MESSAGE,  taskId);
        return new CRMBaseException(message, HttpStatus.NOT_FOUND);
    }

    public CRMBaseException userNotFound(String username) {
        String message = String.format(ErrorMessages.USER_NOT_FOUND_BY_USERNAME_MESSAGE, username);
        return new CRMBaseException(message, HttpStatus.NOT_FOUND);
    }

    public CRMBaseException userAlreadyExists(String username) {
        String message = String.format(ErrorMessages.USER_ALREADY_EXISTS_MESSAGE, username);
        return new CRMBaseException(message, HttpStatus.CONFLICT);
    }
}
