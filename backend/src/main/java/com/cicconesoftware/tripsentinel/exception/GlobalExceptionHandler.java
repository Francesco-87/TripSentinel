package com.cicconesoftware.tripsentinel.exception;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// Global exception handler that centralizes error handling across the entire application
// Converts custom exceptions into standardized HTTP responses with consistent error format
@ControllerAdvice
public class GlobalExceptionHandler {

    // Handles ResourceNotFoundException: converts 404 errors to standardized response
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
    }

    // Handles BadRequestException: converts 400 errors to standardized response
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());
    }

    // Handles ConflictException: converts 409 errors to standardized response
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex) {
        return buildResponse(HttpStatus.CONFLICT, "Conflict", ex.getMessage());
    }

    // Helper method to avoid code repetition; builds consistent error response structure
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String error, String message) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(status.value(), error, message, OffsetDateTime.now()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {

    String message = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));

    return buildResponse(
            HttpStatus.BAD_REQUEST,
            "Bad Request",
            message
        );
    }
}