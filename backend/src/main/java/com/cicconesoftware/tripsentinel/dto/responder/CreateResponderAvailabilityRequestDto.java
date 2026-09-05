package com.cicconesoftware.tripsentinel.dto.responder;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

/** Carries data for creating a responder availability slot. */
public class CreateResponderAvailabilityRequestDto {
    
    @NotNull
    private LocalDateTime availableFrom;

    @NotNull
    private LocalDateTime availableUntil;

    @NotBlank
    private String timeZone;

    public LocalDateTime getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(LocalDateTime availableFrom) {
        this.availableFrom = availableFrom;
    }

    public LocalDateTime getAvailableUntil() {
        return availableUntil;
    }

    public void setAvailableUntil(LocalDateTime availableUntil) {
        this.availableUntil = availableUntil;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

        
}
