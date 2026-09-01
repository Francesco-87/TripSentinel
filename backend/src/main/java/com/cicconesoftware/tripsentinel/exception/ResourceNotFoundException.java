package com.cicconesoftware.tripsentinel.exception;

// Custom exception thrown when a requested resource (entity) is not found in the database
// Maps to HTTP 404 Not Found response
public class ResourceNotFoundException extends RuntimeException {

    // Constructor that accepts a descriptive error message
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
}