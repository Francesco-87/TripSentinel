package com.cicconesoftware.tripsentinel.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cicconesoftware.tripsentinel.dto.session.SessionEventResponseDto;
import com.cicconesoftware.tripsentinel.service.session.SessionEventService;

@RestController
@RequestMapping("/api/session-events")
@CrossOrigin(origins = "http://localhost:5173")
/** Exposes HTTP endpoints for session event operations. */
public class SessionEventController {

    private final SessionEventService sessionEventService;

    public SessionEventController(SessionEventService sessionEventService) {
        this.sessionEventService = sessionEventService;
    }

    @GetMapping("/{id}")
    public SessionEventResponseDto getSessionEventById(@PathVariable Long id) {
        return sessionEventService.getById(id);
    }

    @GetMapping("/by-session/{sessionId}")
    public List<SessionEventResponseDto> getSessionEventsBySession(
            @PathVariable Long sessionId) {

        return sessionEventService.getBySessionId(sessionId);
    }
}
