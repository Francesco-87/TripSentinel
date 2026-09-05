package com.cicconesoftware.tripsentinel.exception;

/** Signals a bad request application error. */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
    
}
