package com.uber.driver.dao;

import org.springframework.http.HttpStatus;

public class DatabaseFailureException extends DemoException{
    public DatabaseFailureException(String message, int responseCode) {
        super(message, responseCode);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}