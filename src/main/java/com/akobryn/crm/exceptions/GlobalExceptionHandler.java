package com.akobryn.crm.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CRMBaseException.class)
    public ResponseEntity<String> handleCRMBaseException(CRMBaseException ex) {
        return ResponseEntity
                .status(ex.errorDetails.getHTTPStatus())
                .body(ex.getMessage());
    }
}
