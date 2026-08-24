package com.cicconesoftware.tripsentinel.service.session;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cicconesoftware.tripsentinel.dto.session.SessionEventResponseDto;
import com.cicconesoftware.tripsentinel.entity.SessionEvent;
import com.cicconesoftware.tripsentinel.mapper.session.SessionEventMapper;
import com.cicconesoftware.tripsentinel.repository.SessionEventRepository;

@Service
public class SessionEventServiceImpl implements SessionEventService {

    private final SessionEventRepository repository;
    private final SessionEventMapper mapper;

    public SessionEventServiceImpl(SessionEventRepository repository, SessionEventMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    @Override
    public SessionEventResponseDto getById(Long id) {
        
        SessionEvent sessionEvent = repository.findById(id)
            .orElseThrow();

        return mapper.toSessionEventResponseDto(sessionEvent); 
    }
    
    @Override
    public List<SessionEventResponseDto> getBySessionId(Long sessionId) {
        List<SessionEvent> sessionEvents = repository.findBySessionId(sessionId);

        return sessionEvents.stream()
            .map(mapper::toSessionEventResponseDto)
            .toList();
    }

    /**TODO: Implement the following methods in the future if needed
     * SessionEvent Session_CANCELED in the CheckInSessionServiceImpl class
     */
}
