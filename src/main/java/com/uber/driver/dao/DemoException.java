package com.uber.driver.dao;

import org.springframework.http.HttpStatus;

public abstract class DemoException  extends RuntimeException{
    private int responseCode;

    public DemoException(String message, int responseCode) {
        super(message);
        this.responseCode = responseCode;
    }

    public abstract HttpStatus getHttpStatus();

    public int getResponseCode() { return responseCode; }
}
