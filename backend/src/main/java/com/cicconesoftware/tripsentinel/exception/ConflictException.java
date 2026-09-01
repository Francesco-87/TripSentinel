package com.cicconesoftware.tripsentinel.exception;


// Custom exception thrown when a request conflicts with existing data or system state
// Commonly used for duplicate entries (e.g., duplicate email address) or invalid state transitions
// Maps to HTTP 409 Conflict response
public class ConflictException extends RuntimeException {

    // Constructor that accepts a descriptive error message
    public ConflictException(String message) {
        super(message);
    }
    
}