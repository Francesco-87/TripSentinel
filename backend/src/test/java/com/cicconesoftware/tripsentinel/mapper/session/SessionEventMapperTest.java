package com.cicconesoftware.tripsentinel.mapper.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.cicconesoftware.tripsentinel.dto.session.SessionEventResponseDto;
import com.cicconesoftware.tripsentinel.entity.SessionEvent;
import com.cicconesoftware.tripsentinel.entity.enums.SessionEventType;

class SessionEventMapperTest {

    private final SessionEventMapper sessionEventMapper = new SessionEventMapper();

    @Test
    void shouldMapSessionEventToSessionEventResponseDto() {
        // Arrange
        Instant createdAt = Instant.now();

        SessionEvent sessionEvent = new SessionEvent();
        sessionEvent.setEventType(SessionEventType.CREATED);
        sessionEvent.setNote("Session created successfully.");
        sessionEvent.setCreatedAt(createdAt);

        // Act
        SessionEventResponseDto responseDto =
                sessionEventMapper.toSessionEventResponseDto(sessionEvent);

        // Assert
        assertNull(responseDto.getId());
        assertEquals(SessionEventType.CREATED, responseDto.getType());
        assertEquals("Session created successfully.", responseDto.getNote());
        assertEquals(createdAt, responseDto.getCreatedAt());
    }

    @Test
    void shouldReturnNullWhenSessionEventIsNull() {
        // Act
        SessionEventResponseDto responseDto =
                sessionEventMapper.toSessionEventResponseDto(null);

        // Assert
        assertNull(responseDto);
    }
}
