package com.cicconesoftware.tripsentinel.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cicconesoftware.tripsentinel.dto.session.SessionEventResponseDto;
import com.cicconesoftware.tripsentinel.entity.enums.SessionEventType;
import com.cicconesoftware.tripsentinel.service.session.SessionEventService;

class SessionEventControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SessionEventService sessionEventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(new SessionEventController(sessionEventService))
                .build();
    }

    @Test
    void shouldGetSessionEventById() throws Exception {
        SessionEventResponseDto responseDto = createResponseDto();

        when(sessionEventService.getById(1L))
                .thenReturn(responseDto);

        mockMvc.perform(get("/api/session-events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.type").value("CREATED"))
                .andExpect(jsonPath("$.note").value("Session created"));

        verify(sessionEventService).getById(1L);
    }

    @Test
    void shouldGetSessionEventsBySessionId() throws Exception {
        SessionEventResponseDto responseDto = createResponseDto();

        when(sessionEventService.getBySessionId(10L))
                .thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/session-events/by-session/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].type").value("CREATED"))
                .andExpect(jsonPath("$[0].note").value("Session created"));

        verify(sessionEventService).getBySessionId(10L);
    }

    private SessionEventResponseDto createResponseDto() {
        return new SessionEventResponseDto(
                1L,
                SessionEventType.CREATED,
                "Session created",
                Instant.parse("2026-08-30T08:00:00Z")
        );
    }
}
