package com.cicconesoftware.tripsentinel.dto.responder;

import java.time.LocalDateTime;

import com.cicconesoftware.tripsentinel.entity.enums.AvailabilityStatus;

import jakarta.validation.constraints.NotNull;

public class UpdateResponderAvailabilityRequestDto {
    

     @NotNull
    private LocalDateTime availableFrom;

    @NotNull
    private LocalDateTime availableUntil;

    @NotNull
    private AvailabilityStatus status;

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

    public AvailabilityStatus getStatus() {
        return status;
    }

    public void setStatus(AvailabilityStatus status) {
        this.status = status;
    }

    
}
