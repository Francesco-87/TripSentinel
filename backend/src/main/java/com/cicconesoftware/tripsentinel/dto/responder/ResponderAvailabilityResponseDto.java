package com.cicconesoftware.tripsentinel.dto.responder;

import java.time.LocalDateTime;

import com.cicconesoftware.tripsentinel.entity.enums.AvailabilityStatus;

public class ResponderAvailabilityResponseDto {

    private final Long id;
    private final Long responderId;
    private final LocalDateTime availableFrom;
    private final LocalDateTime availableUntil;
    private final AvailabilityStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ResponderAvailabilityResponseDto(Long id, Long responderId, LocalDateTime availableFrom, LocalDateTime availableUntil, AvailabilityStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.responderId = responderId;
        this.availableFrom = availableFrom;
        this.availableUntil = availableUntil;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getResponderId() {
        return responderId;
    }

    public LocalDateTime getAvailableFrom() {
        return availableFrom;
    }

    public LocalDateTime getAvailableUntil() {
        return availableUntil;
    }

    public AvailabilityStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    
    
}
