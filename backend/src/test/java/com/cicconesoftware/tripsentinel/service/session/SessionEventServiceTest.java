package com.cicconesoftware.tripsentinel.service.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cicconesoftware.tripsentinel.dto.session.SessionEventResponseDto;
import com.cicconesoftware.tripsentinel.entity.SessionEvent;
import com.cicconesoftware.tripsentinel.entity.enums.SessionEventType;
import com.cicconesoftware.tripsentinel.mapper.session.SessionEventMapper;
import com.cicconesoftware.tripsentinel.repository.SessionEventRepository;

class SessionEventServiceTest {

    @Mock
    private SessionEventRepository repository;

    private SessionEventService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        SessionEventMapper mapper = new SessionEventMapper();

        service = new SessionEventServiceImpl(
                repository,
                mapper
        );
    }

    @Test
    void shouldGetSessionEventById() {
        // Arrange
        Instant createdAt = Instant.now();

        SessionEvent event = new SessionEvent();
        event.setEventType(SessionEventType.CREATED);
        event.setNote("Session created.");
        event.setCreatedAt(createdAt);

        when(repository.findById(1L))
                .thenReturn(Optional.of(event));

        // Act
        SessionEventResponseDto result = service.getById(1L);

        // Assert
        assertEquals(SessionEventType.CREATED, result.getType());
        assertEquals("Session created.", result.getNote());
        assertEquals(createdAt, result.getCreatedAt());

        verify(repository).findById(1L);
    }

    @Test
    void shouldGetSessionEventsBySessionId() {
        // Arrange
        SessionEvent created = new SessionEvent();
        created.setEventType(SessionEventType.CREATED);
        created.setNote("Session created.");

        SessionEvent cancelled = new SessionEvent();
        cancelled.setEventType(SessionEventType.CANCELLED);
        cancelled.setNote("Session cancelled.");

        when(repository.findBySessionId(1L))
                .thenReturn(List.of(created, cancelled));

        // Act
        List<SessionEventResponseDto> result =
                service.getBySessionId(1L);

        // Assert
        assertEquals(2, result.size());
        assertEquals(
                SessionEventType.CREATED,
                result.get(0).getType()
        );
        assertEquals(
                SessionEventType.CANCELLED,
                result.get(1).getType()
        );

        verify(repository).findBySessionId(1L);
    }
}
