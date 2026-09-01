package com.cicconesoftware.tripsentinel.exception;

import java.time.OffsetDateTime;

// DTO for standardized error response body sent to client when exceptions occur
// Provides consistent error format across all API endpoints
public class ErrorResponse {

    // HTTP status code (e.g., 404, 400, 409)
    private int status;
    
    // Error type/name (e.g., "Not Found", "Bad Request", "Conflict")
    private String error;
    
    // Detailed error message explaining what went wrong
    private String message; 
    
    // Timestamp when the error occurred
    private OffsetDateTime timestamp;

    // All-args constructor to initialize the error response with all fields
    public ErrorResponse(int status, String error, String message, OffsetDateTime timestamp) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getter methods for all fields
    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

  
    
}