package com.cicconesoftware.tripsentinel.dto.session;

import java.time.LocalDateTime;

import com.cicconesoftware.tripsentinel.entity.enums.SessionEventType;

public class SessionEventResponseDto {

    private final Long id;
    private final SessionEventType type;
    private final String note;
    private final LocalDateTime createdAt;

    public SessionEventResponseDto(Long id, SessionEventType type, String note, LocalDateTime createdAt) {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
