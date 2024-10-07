package com.akobryn.crm.exceptions;

import org.springframework.http.HttpStatus;

public class CRMBaseException extends RuntimeException {

    BaseError errorDetails;

    public CRMBaseException(String message, HttpStatus httpStatus) {
        super(message);
        errorDetails = new BaseException(message, httpStatus);
    }
}