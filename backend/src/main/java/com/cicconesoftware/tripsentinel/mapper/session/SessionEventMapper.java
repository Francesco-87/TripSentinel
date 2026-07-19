package com.cicconesoftware.tripsentinel.mapper.session;

import org.springframework.stereotype.Component;

import com.cicconesoftware.tripsentinel.dto.session.SessionEventResponseDto;
import com.cicconesoftware.tripsentinel.entity.SessionEvent;

@Component
public class SessionEventMapper {

    public SessionEventResponseDto toSessionEventResponseDto(SessionEvent sessionEvent) {
        if (sessionEvent == null) {
            return null;
        }
        return new SessionEventResponseDto(
                sessionEvent.getId(),
                sessionEvent.getEventType(),
                sessionEvent.getNote(),
                sessionEvent.getCreatedAt()
        );
    }
    
}
