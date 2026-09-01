package com.cicconesoftware.tripsentinel.integration;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.cicconesoftware.tripsentinel.entity.CheckInMethod;
import com.cicconesoftware.tripsentinel.entity.enums.CheckInMethodType;
import com.cicconesoftware.tripsentinel.repository.CheckInMethodRepository;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class CheckInMethodIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CheckInMethodRepository checkInMethodRepository;


    @Test
    void shouldGetAllCheckInMethods() throws Exception {

        mockMvc.perform(get("/api/check-in-methods"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[*].name", hasItem("PHONE")));

        assertFalse(checkInMethodRepository.findAll().isEmpty());
    }


    @Test
    void shouldGetCheckInMethodById() throws Exception {

        CheckInMethod method = checkInMethodRepository
                .findByName(CheckInMethodType.PHONE)
                .orElseThrow();

        mockMvc.perform(get("/api/check-in-methods/{id}", method.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(method.getId()))
                .andExpect(jsonPath("$.name").value("PHONE"));
    }


    @Test
    void shouldGetCheckInMethodByName() throws Exception {

        CheckInMethod method = checkInMethodRepository
                .findByName(CheckInMethodType.PHONE)
                .orElseThrow();

        mockMvc.perform(get(
                "/api/check-in-methods/by-name/{name}",
                CheckInMethodType.PHONE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(method.getId()))
                .andExpect(jsonPath("$.name").value("PHONE"));
    }

    @Test
void shouldReturnNotFoundWhenCheckInMethodDoesNotExistById() throws Exception {

    mockMvc.perform(get("/api/check-in-methods/{id}", 999999L))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Not Found"))
            .andExpect(jsonPath("$.message")
                    .value("Check-in method not found with id: 999999"))
            .andExpect(jsonPath("$.timestamp").exists());
}


@Test
void shouldReturnBadRequestForInvalidCheckInMethodName() throws Exception {

    mockMvc.perform(get(
            "/api/check-in-methods/by-name/{name}",
            "UNKNOWN"))
            .andExpect(status().isBadRequest());
}
}