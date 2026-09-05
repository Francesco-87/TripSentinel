package com.cicconesoftware.tripsentinel.dto.responder;

import java.time.Instant;

import com.cicconesoftware.tripsentinel.entity.enums.AvailabilityStatus;

/** Represents responder availability data returned by the API. */
public class ResponderAvailabilityResponseDto {

    private final Long id;
    private final Long responderId;
    private final Instant availableFrom;
    private final Instant availableUntil;
    private final String timeZone;
    private final AvailabilityStatus status;
    private final Instant createdAt;
    private final Instant updatedAt;

    public ResponderAvailabilityResponseDto(Long id, Long responderId, Instant availableFrom, Instant availableUntil,
            String timeZone, AvailabilityStatus status, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.responderId = responderId;
        this.availableFrom = availableFrom;
        this.availableUntil = availableUntil;
        this.timeZone = timeZone;
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

    public Instant getAvailableFrom() {
        return availableFrom;
    }

    public Instant getAvailableUntil() {
        return availableUntil;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public AvailabilityStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    
    
}
