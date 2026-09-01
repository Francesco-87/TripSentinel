package com.cicconesoftware.tripsentinel.exception;

// Custom exception thrown when the client sends invalid or malformed request data
// Maps to HTTP 400 Bad Request response
public class BadRequestException extends RuntimeException {

    // Constructor that accepts a descriptive error message
    public BadRequestException(String message) {
        super(message);
    }
    
}