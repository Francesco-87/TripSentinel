package com.cicconesoftware.tripsentinel.dto.responder;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public class CreateResponderAvailabilityRequestDto {
    
    @NotNull
    private LocalDateTime availableFrom;

    @NotNull
    private LocalDateTime availableUntil;

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

        
}
