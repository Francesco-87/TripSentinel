package com.cicconesoftware.tripsentinel.exception;

import java.time.OffsetDateTime;

/** Represents the standard error body returned by the API. */
public class ErrorResponse {

    private int status;
    
    private String error;
    
    private String message; 
    
    private OffsetDateTime timestamp;

    public ErrorResponse(int status, String error, String message, OffsetDateTime timestamp) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
    }

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
