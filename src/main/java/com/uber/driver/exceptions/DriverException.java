package com.uber.driver.exceptions;

import org.springframework.http.HttpStatus;

public abstract class DriverException extends RuntimeException{
    private int responseCode;

    public DriverException(String errMsg, int responseCode) {
        super(errMsg);
        this.responseCode = responseCode;
    }

    public abstract HttpStatus getHttpStatus();

    public int getResponseCode() {
        return responseCode;
    }
}
