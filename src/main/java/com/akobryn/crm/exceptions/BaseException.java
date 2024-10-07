package com.akobryn.crm.exceptions;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class BaseException implements BaseError {
    private String errorMessage;
    private HttpStatus httpStatus;

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public HttpStatus getHTTPStatus() {
        return httpStatus;
    }
}
