package com.cicconesoftware.tripsentinel.dto.session;

import java.time.Instant;
import java.util.Set;

import com.cicconesoftware.tripsentinel.dto.checkinmethod.CheckInMethodResponseDto;
import com.cicconesoftware.tripsentinel.entity.enums.SessionStatus;

/** Represents check in session data returned by the API. */
public class CheckInSessionResponseDto {

    private final Long id;
    private final Long customerId;
    private final Long responderId;
    private final Set<SessionEventResponseDto> events;
    private final Set<CheckInMethodResponseDto> checkInMethods;
    private final Instant startAt;
    private final Instant expectedReturnAt;
    private final Instant latestCheckInAt;
    private final String timeZone;
    private final String locationDescription;
    private final String importantNotes;
    private final SessionStatus status;
    private final Instant createdAt;
    private final Instant updatedAt;

    public CheckInSessionResponseDto(Long id, Long customerId, Long responderId, Set<SessionEventResponseDto> events,
            Set<CheckInMethodResponseDto> checkInMethods, Instant startAt, Instant expectedReturnAt,
            Instant latestCheckInAt, String timeZone, String locationDescription, String importantNotes,
            SessionStatus status, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.responderId = responderId;
        this.events = events;
        this.checkInMethods = checkInMethods;
        this.startAt = startAt;
        this.expectedReturnAt = expectedReturnAt;
        this.latestCheckInAt = latestCheckInAt;
        this.timeZone = timeZone;
        this.locationDescription = locationDescription;
        this.importantNotes = importantNotes;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getResponderId() {
        return responderId;
    }

    public Set<SessionEventResponseDto> getEvents() {
        return events;
    }

    public Set<CheckInMethodResponseDto> getCheckInMethods() {
        return checkInMethods;
    }

    public Instant getStartAt() {
        return startAt;
    }

    public Instant getExpectedReturnAt() {
        return expectedReturnAt;
    }

    /** Returns the deadline after which a missed return may be escalated. */
    public Instant getLatestCheckInAt() {
        return latestCheckInAt;
    }

    public String getTimeZone() {
        return timeZone;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }    
    
}
