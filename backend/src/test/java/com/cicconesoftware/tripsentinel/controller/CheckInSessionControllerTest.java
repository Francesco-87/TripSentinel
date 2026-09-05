package com.cicconesoftware.tripsentinel.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cicconesoftware.tripsentinel.dto.checkinmethod.CheckInMethodResponseDto;
import com.cicconesoftware.tripsentinel.dto.session.AdminCreateCheckInSessionRequestDto;
import com.cicconesoftware.tripsentinel.dto.session.CheckInSessionResponseDto;
import com.cicconesoftware.tripsentinel.dto.session.CreateCheckInSessionRequestDto;
import com.cicconesoftware.tripsentinel.dto.session.UpdateCheckInSessionRequestDto;
import com.cicconesoftware.tripsentinel.entity.enums.CheckInMethodType;
import com.cicconesoftware.tripsentinel.entity.enums.SessionStatus;
import com.cicconesoftware.tripsentinel.service.session.CheckInSessionService;

import tools.jackson.databind.ObjectMapper;

class CheckInSessionControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private CheckInSessionService checkInSessionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders
                .standaloneSetup(new CheckInSessionController(checkInSessionService))
                .build();
    }

    @Test
    void shouldGetAllCheckInSessions() throws Exception {
        CheckInSessionResponseDto responseDto = createResponseDto();

        when(checkInSessionService.getAll())
                .thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/check-in-sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10L))
                .andExpect(jsonPath("$[0].customerId").value(1L))
                .andExpect(jsonPath("$[0].responderId").value(2L))
                .andExpect(jsonPath("$[0].status").value("PLANNED"));

        verify(checkInSessionService).getAll();
    }

    @Test
    void shouldGetCheckInSessionById() throws Exception {
        CheckInSessionResponseDto responseDto = createResponseDto();

        when(checkInSessionService.getById(10L))
                .thenReturn(responseDto);

        mockMvc.perform(get("/api/check-in-sessions/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.customerId").value(1L))
                .andExpect(jsonPath("$.responderId").value(2L));

        verify(checkInSessionService).getById(10L);
    }

    @Test
    void shouldGetCheckInSessionsByUser() throws Exception {
        CheckInSessionResponseDto responseDto = createResponseDto();

        when(checkInSessionService.getByUserId(1L))
                .thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/check-in-sessions/by-user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10L))
                .andExpect(jsonPath("$[0].customerId").value(1L));

        verify(checkInSessionService).getByUserId(1L);
    }

    @Test
    void shouldGetCheckInSessionsByResponder() throws Exception {
        CheckInSessionResponseDto responseDto = createResponseDto();

        when(checkInSessionService.getByResponderId(2L))
                .thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/check-in-sessions/by-responder/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10L))
                .andExpect(jsonPath("$[0].responderId").value(2L));

        verify(checkInSessionService).getByResponderId(2L);
    }

    @Test
    void shouldAdminCreateCheckInSession() throws Exception {
        AdminCreateCheckInSessionRequestDto requestDto =
                new AdminCreateCheckInSessionRequestDto();

        requestDto.setCustomerId(1L);
        requestDto.setResponderId(2L);
        requestDto.setCheckInMethodIds(Set.of(1L));
        requestDto.setStartAt(LocalDateTime.of(2026, 9, 1, 8, 0));
        requestDto.setExpectedReturnAt(LocalDateTime.of(2026, 9, 1, 16, 0));
        requestDto.setLatestCheckInAt(LocalDateTime.of(2026, 9, 1, 17, 0));
        requestDto.setTimeZone("Europe/Oslo");
        requestDto.setLocationDescription("Nordmarka south");
        requestDto.setImportantNotes("Test notes");

        CheckInSessionResponseDto responseDto = createResponseDto();

        when(checkInSessionService.adminCreateCheckInSession(
                any(AdminCreateCheckInSessionRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/check-in-sessions/create-admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.customerId").value(1L))
                .andExpect(jsonPath("$.responderId").value(2L))
                .andExpect(jsonPath("$.status").value("PLANNED"));

        verify(checkInSessionService)
                .adminCreateCheckInSession(
                        any(AdminCreateCheckInSessionRequestDto.class));
    }

    @Test
    void shouldCreateCheckInSessionForUser() throws Exception {
        CreateCheckInSessionRequestDto requestDto =
                new CreateCheckInSessionRequestDto();

        requestDto.setResponderId(2L);
        requestDto.setCheckInMethodIds(Set.of(1L));
        requestDto.setStartAt(LocalDateTime.of(2026, 9, 1, 8, 0));
        requestDto.setExpectedReturnAt(LocalDateTime.of(2026, 9, 1, 16, 0));
        requestDto.setLatestCheckInAt(LocalDateTime.of(2026, 9, 1, 17, 0));
        requestDto.setTimeZone("Europe/Oslo");
        requestDto.setLocationDescription("Nordmarka south");
        requestDto.setImportantNotes("Test notes");

        CheckInSessionResponseDto responseDto = createResponseDto();

        when(checkInSessionService.createCheckInSessionForUser(
                any(CreateCheckInSessionRequestDto.class),
                eq(1L)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/check-in-sessions/create-user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.customerId").value(1L));

        verify(checkInSessionService)
                .createCheckInSessionForUser(
                        any(CreateCheckInSessionRequestDto.class),
                        eq(1L));
    }

    @Test
    void shouldUpdateCheckInSession() throws Exception {
        UpdateCheckInSessionRequestDto requestDto =
                new UpdateCheckInSessionRequestDto();

        requestDto.setResponderId(2L);
        requestDto.setCheckInMethodIds(Set.of(1L));
        requestDto.setStartAt(LocalDateTime.of(2026, 9, 1, 9, 0));
        requestDto.setExpectedReturnAt(LocalDateTime.of(2026, 9, 1, 18, 0));
        requestDto.setLatestCheckInAt(LocalDateTime.of(2026, 9, 1, 19, 0));
        requestDto.setTimeZone("Europe/Oslo");
        requestDto.setLocationDescription("Nordmarka north");
        requestDto.setImportantNotes("Updated notes");

        CheckInSessionResponseDto responseDto = createResponseDto();

        when(checkInSessionService.updateCheckInSession(
                any(UpdateCheckInSessionRequestDto.class),
                eq(10L)))
                .thenReturn(responseDto);

        mockMvc.perform(patch("/api/check-in-sessions/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L));

        verify(checkInSessionService)
                .updateCheckInSession(
                        any(UpdateCheckInSessionRequestDto.class),
                        eq(10L));
    }

    @Test
    void shouldCancelCheckInSession() throws Exception {
        when(checkInSessionService.cancelCheckInSession(10L))
                .thenReturn(createResponseDto());

        mockMvc.perform(delete("/api/check-in-sessions/10"))
                .andExpect(status().isNoContent());

        verify(checkInSessionService).cancelCheckInSession(10L);
    }

    private CheckInSessionResponseDto createResponseDto() {
        CheckInMethodResponseDto method =
                new CheckInMethodResponseDto(
                        1L,
                        CheckInMethodType.PHONE);

        return new CheckInSessionResponseDto(
                10L,
                1L,
                2L,
                Set.of(),
                Set.of(method),
                Instant.parse("2026-09-01T06:00:00Z"),
                Instant.parse("2026-09-01T14:00:00Z"),
                Instant.parse("2026-09-01T15:00:00Z"),
                "Europe/Oslo",
                "Nordmarka south",
                "Test notes",
                SessionStatus.PLANNED,
                Instant.parse("2026-08-30T08:00:00Z"),
                Instant.parse("2026-08-30T08:00:00Z")
        );
    }
}
