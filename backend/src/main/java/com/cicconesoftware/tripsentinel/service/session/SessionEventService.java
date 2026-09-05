package com.cicconesoftware.tripsentinel.service.session;

import java.util.List;

import com.cicconesoftware.tripsentinel.dto.session.SessionEventResponseDto;

/** Defines operations for managing session event data. */
public interface SessionEventService {

    /** Returns one session event or fails when the ID does not exist. */
    SessionEventResponseDto getById(Long id);

    /** Returns all events for a session; no result order is guaranteed. */
    List<SessionEventResponseDto> getBySessionId(Long sessionId);
}
