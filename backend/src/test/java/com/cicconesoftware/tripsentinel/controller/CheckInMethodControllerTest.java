package com.cicconesoftware.tripsentinel.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cicconesoftware.tripsentinel.dto.checkinmethod.CheckInMethodResponseDto;
import com.cicconesoftware.tripsentinel.entity.enums.CheckInMethodType;
import com.cicconesoftware.tripsentinel.service.checkinmethod.CheckInMethodService;

class CheckInMethodControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CheckInMethodService checkInMethodService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(new CheckInMethodController(checkInMethodService))
                .build();
    }

    @Test
    void shouldGetAllCheckInMethods() throws Exception {
        CheckInMethodResponseDto responseDto =
                new CheckInMethodResponseDto(
                        1L,
                        CheckInMethodType.PHONE);

        when(checkInMethodService.getAll())
                .thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/check-in-methods"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("PHONE"));

        verify(checkInMethodService).getAll();
    }

    @Test
    void shouldGetCheckInMethodById() throws Exception {
        CheckInMethodResponseDto responseDto =
                new CheckInMethodResponseDto(
                        1L,
                        CheckInMethodType.PHONE);

        when(checkInMethodService.getById(1L))
                .thenReturn(responseDto);

        mockMvc.perform(get("/api/check-in-methods/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("PHONE"));

        verify(checkInMethodService).getById(1L);
    }

    @Test
    void shouldGetCheckInMethodByName() throws Exception {
        CheckInMethodResponseDto responseDto =
                new CheckInMethodResponseDto(
                        1L,
                        CheckInMethodType.PHONE);

        when(checkInMethodService.getByName(CheckInMethodType.PHONE))
                .thenReturn(responseDto);

        mockMvc.perform(get("/api/check-in-methods/by-name/PHONE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("PHONE"));

        verify(checkInMethodService)
                .getByName(CheckInMethodType.PHONE);
    }
}