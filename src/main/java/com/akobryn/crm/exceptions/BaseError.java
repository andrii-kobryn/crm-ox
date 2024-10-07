package com.akobryn.crm.exceptions;

import org.springframework.http.HttpStatus;

public interface BaseError {

    String getErrorMessage();

    HttpStatus getHTTPStatus();
}
