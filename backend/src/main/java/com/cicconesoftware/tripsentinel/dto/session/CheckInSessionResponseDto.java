package com.cicconesoftware.tripsentinel.dto.session;

import java.time.LocalDateTime;
import java.util.Set;

import com.cicconesoftware.tripsentinel.dto.checkinmethod.CheckInMethodResponseDto;
import com.cicconesoftware.tripsentinel.entity.enums.SessionStatus;

public class CheckInSessionResponseDto {

    private final long id;
    private final long customerId;
    private final long responderId;
    private final Set<SessionEventResponseDto> events;
    private final Set<CheckInMethodResponseDto> checkInMethods;
    private final LocalDateTime startAt;
    private final LocalDateTime expectedReturnAt;
    private final LocalDateTime latestCheckInAt;
    private final String locationDescription;
    private final String importantNotes;
    private final SessionStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public CheckInSessionResponseDto(long id, long customerId, long responderId, Set<SessionEventResponseDto> events,
            Set<CheckInMethodResponseDto> checkInMethods, LocalDateTime startAt, LocalDateTime expectedReturnAt,
            LocalDateTime latestCheckInAt, String locationDescription, String importantNotes, SessionStatus status,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.responderId = responderId;
        this.events = events;
        this.checkInMethods = checkInMethods;
        this.startAt = startAt;
        this.expectedReturnAt = expectedReturnAt;
        this.latestCheckInAt = latestCheckInAt;
        this.locationDescription = locationDescription;
        this.importantNotes = importantNotes;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getId() {
        return id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public long getResponderId() {
        return responderId;
    }

    public Set<SessionEventResponseDto> getEvents() {
        return events;
    }

    public Set<CheckInMethodResponseDto> getCheckInMethods() {
        return checkInMethods;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public LocalDateTime getExpectedReturnAt() {
        return expectedReturnAt;
    }

    public LocalDateTime getLatestCheckInAt() {
        return latestCheckInAt;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public String getImportantNotes() {
        return importantNotes;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }    
    
}
