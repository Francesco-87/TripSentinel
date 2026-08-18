package com.cicconesoftware.tripsentinel.service.session;

import java.util.List;

import com.cicconesoftware.tripsentinel.dto.session.SessionEventResponseDto;

public interface SessionEventService {

    SessionEventResponseDto getById(Long id);

List<SessionEventResponseDto> getBySessionId(Long sessionId);
    
}
