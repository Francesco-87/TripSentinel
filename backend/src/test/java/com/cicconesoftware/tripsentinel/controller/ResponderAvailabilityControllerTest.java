package com.cicconesoftware.tripsentinel.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cicconesoftware.tripsentinel.dto.responder.CreateResponderAvailabilityRequestDto;
import com.cicconesoftware.tripsentinel.dto.responder.ResponderAvailabilityResponseDto;
import com.cicconesoftware.tripsentinel.dto.responder.UpdateResponderAvailabilityRequestDto;
import com.cicconesoftware.tripsentinel.entity.enums.AvailabilityStatus;
import com.cicconesoftware.tripsentinel.service.responder.ResponderAvailabilityService;

import tools.jackson.databind.ObjectMapper;

class ResponderAvailabilityControllerTest {

    @Mock
    private ResponderAvailabilityService responderAvailabilityService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ResponderAvailabilityController controller =
                new ResponderAvailabilityController(
                        responderAvailabilityService
                );

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldCreateResponderAvailability() throws Exception {
        // Arrange
        LocalDateTime availableFrom =
                LocalDateTime.of(2026, 9, 1, 8, 0);

        LocalDateTime availableUntil =
                LocalDateTime.of(2026, 9, 1, 16, 0);

        CreateResponderAvailabilityRequestDto dto =
                new CreateResponderAvailabilityRequestDto();

        dto.setAvailableFrom(availableFrom);
        dto.setAvailableUntil(availableUntil);
        dto.setTimeZone("Europe/Oslo");

        ResponderAvailabilityResponseDto response =
                createResponseDto();

        when(responderAvailabilityService.create(
                eq(1L),
                any(CreateResponderAvailabilityRequestDto.class)
        )).thenReturn(response);

        // Act + Assert
        mockMvc.perform(post(
                        "/api/responder-availability/responder/1"
                )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(dto)
                        ))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.responderId").value(1))
                .andExpect(
                        jsonPath("$.status")
                                .value("AVAILABLE")
                );

        verify(responderAvailabilityService).create(
                eq(1L),
                any(CreateResponderAvailabilityRequestDto.class)
        );
    }

    @Test
    void shouldGetAllResponderAvailability() throws Exception {
        // Arrange
        ResponderAvailabilityResponseDto first =
                createResponseDto();

        ResponderAvailabilityResponseDto second =
                new ResponderAvailabilityResponseDto(
                        11L,
                        2L,
                        Instant.parse("2026-09-02T06:00:00Z"),
                        Instant.parse("2026-09-02T14:00:00Z"),
                        "Europe/Oslo",
                        AvailabilityStatus.UNAVAILABLE,
                        Instant.parse("2026-08-30T08:00:00Z"),
                        Instant.parse("2026-08-30T08:00:00Z")
                );

        when(responderAvailabilityService.getAll())
                .thenReturn(List.of(first, second));

        // Act + Assert
        mockMvc.perform(get(
                        "/api/responder-availability"
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[1].id").value(11))
                .andExpect(
                        jsonPath("$[0].status")
                                .value("AVAILABLE")
                )
                .andExpect(
                        jsonPath("$[1].status")
                                .value("UNAVAILABLE")
                );

        verify(responderAvailabilityService).getAll();
    }

    @Test
    void shouldGetResponderAvailabilityById() throws Exception {
        // Arrange
        ResponderAvailabilityResponseDto response =
                createResponseDto();

        when(responderAvailabilityService.getById(10L))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(get(
                        "/api/responder-availability/10"
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.responderId").value(1))
                .andExpect(
                        jsonPath("$.status")
                                .value("AVAILABLE")
                );

        verify(responderAvailabilityService)
                .getById(10L);
    }

    @Test
    void shouldGetResponderAvailabilityByResponderId()
            throws Exception {

        // Arrange
        ResponderAvailabilityResponseDto first =
                createResponseDto();

        ResponderAvailabilityResponseDto second =
                new ResponderAvailabilityResponseDto(
                        11L,
                        1L,
                        Instant.parse("2026-09-02T06:00:00Z"),
                        Instant.parse("2026-09-02T14:00:00Z"),
                        "Europe/Oslo",
                        AvailabilityStatus.AVAILABLE,
                        Instant.parse("2026-08-30T08:00:00Z"),
                        Instant.parse("2026-08-30T08:00:00Z")
                );

        when(responderAvailabilityService
                .getByResponderId(1L))
                .thenReturn(List.of(first, second));

        // Act + Assert
        mockMvc.perform(get(
                        "/api/responder-availability/responder/1"
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(
                        jsonPath("$[0].responderId")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$[1].responderId")
                                .value(1)
                );

        verify(responderAvailabilityService)
                .getByResponderId(1L);
    }

    @Test
    void shouldUpdateResponderAvailability()
            throws Exception {

        // Arrange
        LocalDateTime availableFrom =
                LocalDateTime.of(2026, 9, 2, 10, 0);

        LocalDateTime availableUntil =
                LocalDateTime.of(2026, 9, 2, 18, 0);

        UpdateResponderAvailabilityRequestDto dto =
                new UpdateResponderAvailabilityRequestDto();

        dto.setAvailableFrom(availableFrom);
        dto.setAvailableUntil(availableUntil);
        dto.setStatus(AvailabilityStatus.UNAVAILABLE);
        dto.setTimeZone("Europe/Oslo");

        ResponderAvailabilityResponseDto response =
                new ResponderAvailabilityResponseDto(
                        10L,
                        1L,
                        Instant.parse("2026-09-02T08:00:00Z"),
                        Instant.parse("2026-09-02T16:00:00Z"),
                        "Europe/Oslo",
                        AvailabilityStatus.UNAVAILABLE,
                        Instant.parse("2026-08-30T08:00:00Z"),
                        Instant.parse("2026-08-30T09:00:00Z")
                );

        when(responderAvailabilityService.update(
                eq(10L),
                any(UpdateResponderAvailabilityRequestDto.class)
        )).thenReturn(response);

        // Act + Assert
        mockMvc.perform(put(
                        "/api/responder-availability/10"
                )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(dto)
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(
                        jsonPath("$.status")
                                .value("UNAVAILABLE")
                );

        verify(responderAvailabilityService).update(
                eq(10L),
                any(UpdateResponderAvailabilityRequestDto.class)
        );
    }

    @Test
    void shouldDeleteResponderAvailability()
            throws Exception {

        // Act + Assert
        mockMvc.perform(delete(
                        "/api/responder-availability/10"
                ))
                .andExpect(status().isNoContent());

        verify(responderAvailabilityService)
                .delete(10L);
    }

    private ResponderAvailabilityResponseDto createResponseDto() {
        return new ResponderAvailabilityResponseDto(
                10L,
                1L,
                Instant.parse("2026-09-01T06:00:00Z"),
                Instant.parse("2026-09-01T14:00:00Z"),
                "Europe/Oslo",
                AvailabilityStatus.AVAILABLE,
                Instant.parse("2026-08-30T08:00:00Z"),
                Instant.parse("2026-08-30T08:00:00Z")
        );
    }
}
