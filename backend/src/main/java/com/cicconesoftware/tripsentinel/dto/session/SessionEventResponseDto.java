package com.cicconesoftware.tripsentinel.dto.session;

import java.time.Instant;

import com.cicconesoftware.tripsentinel.entity.enums.SessionEventType;

/** Represents session event data returned by the API. */
public class SessionEventResponseDto {

    private final Long id;
    private final SessionEventType type;
    private final String note;
    private final Instant createdAt;

    public SessionEventResponseDto(Long id, SessionEventType type, String note, Instant createdAt) {
        this.id = id;
        this.type = type;
        this.note = note;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
        }   

    public SessionEventType getType() {
        return type;
    }

    public String getNote() {
        return note;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

}
