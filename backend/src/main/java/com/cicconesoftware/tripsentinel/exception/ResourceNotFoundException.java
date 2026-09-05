package com.cicconesoftware.tripsentinel.exception;

/** Signals a resource not found application error. */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
    
}
