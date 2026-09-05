package com.cicconesoftware.tripsentinel.exception;


/** Signals a conflict application error. */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
    
}
