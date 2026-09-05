package com.cicconesoftware.tripsentinel.dto.responder;

import java.time.LocalDateTime;

import com.cicconesoftware.tripsentinel.entity.enums.AvailabilityStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

/** Carries data for updating a responder availability slot. */
public class UpdateResponderAvailabilityRequestDto {
    

     @NotNull
    private LocalDateTime availableFrom;

    @NotNull
    private LocalDateTime availableUntil;

    @NotNull
    private AvailabilityStatus status;

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

    public AvailabilityStatus getStatus() {
        return status;
    }

    public void setStatus(AvailabilityStatus status) {
        this.status = status;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    
}
