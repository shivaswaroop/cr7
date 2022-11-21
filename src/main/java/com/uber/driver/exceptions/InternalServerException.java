package com.uber.driver.exceptions;

import org.springframework.http.HttpStatus;

public class InternalServerException extends DriverException{

    public InternalServerException(String errMsg, int responseCode) {
        super(errMsg, responseCode);
    }
    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
