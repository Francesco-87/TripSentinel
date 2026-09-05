package com.cicconesoftware.tripsentinel.dto.session;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** Carries administrative data for creating a check-in session. */
public class AdminCreateCheckInSessionRequestDto {

    @NotNull
    private Long customerId;

    @NotNull
    private Long responderId;

    @NotEmpty
    private Set<Long> checkInMethodIds;

    @NotNull
    private LocalDateTime startAt;

    @NotNull
    private LocalDateTime expectedReturnAt;

    @NotNull
    private LocalDateTime latestCheckInAt;

    @NotBlank
    private String timeZone;

    
    private String locationDescription;

    private String importantNotes;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getResponderId() {
        return responderId;
    }

    public void setResponderId(Long responderId) {
        this.responderId = responderId;
    }

    public Set<Long> getCheckInMethodIds() {
        return checkInMethodIds;
    }

    public void setCheckInMethodIds(Set<Long> checkInMethodIds) {
        this.checkInMethodIds = checkInMethodIds;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public LocalDateTime getExpectedReturnAt() {
        return expectedReturnAt;
    }

    public void setExpectedReturnAt(LocalDateTime expectedReturnAt) {
        this.expectedReturnAt = expectedReturnAt;
    }

    public LocalDateTime getLatestCheckInAt() {
        return latestCheckInAt;
    }

    public void setLatestCheckInAt(LocalDateTime latestCheckInAt) {
        this.latestCheckInAt = latestCheckInAt;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public String getImportantNotes() {
        return importantNotes;
    }

    public void setImportantNotes(String importantNotes) {
        this.importantNotes = importantNotes;
    }    

    
    
}
