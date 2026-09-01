package com.cicconesoftware.tripsentinel.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.cicconesoftware.tripsentinel.dto.responder.CreateResponderAvailabilityRequestDto;
import com.cicconesoftware.tripsentinel.dto.responder.UpdateResponderAvailabilityRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.AdminCreateUserRequestDto;
import com.cicconesoftware.tripsentinel.entity.ResponderAvailability;
import com.cicconesoftware.tripsentinel.entity.enums.AvailabilityStatus;
import com.cicconesoftware.tripsentinel.entity.enums.RoleType;
import com.cicconesoftware.tripsentinel.entity.enums.UserStatus;
import com.cicconesoftware.tripsentinel.repository.ResponderAvailabilityRepository;
import com.cicconesoftware.tripsentinel.repository.UserRepository;

import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class ResponderAvailabilityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResponderAvailabilityRepository responderAvailabilityRepository;


    @Test
    void shouldCreateResponderAvailability() throws Exception {

        Long responderId = createResponder("availability.create@test.com");

        CreateResponderAvailabilityRequestDto dto =
                new CreateResponderAvailabilityRequestDto();

        dto.setAvailableFrom(LocalDateTime.of(2026, 9, 1, 8, 0));
        dto.setAvailableUntil(LocalDateTime.of(2026, 9, 1, 16, 0));

        mockMvc.perform(post(
                "/api/responder-availability/responder/{responderId}",
                responderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.responderId").value(responderId))
                .andExpect(jsonPath("$.status").value("AVAILABLE"))
                .andExpect(jsonPath("$.availableFrom").exists())
                .andExpect(jsonPath("$.availableUntil").exists());

        var records =
                responderAvailabilityRepository.findByResponderId(responderId);

        assertEquals(1, records.size());
        assertEquals(AvailabilityStatus.AVAILABLE, records.get(0).getStatus());
    }


    @Test
    void shouldGetResponderAvailabilityById() throws Exception {

        Long responderId = createResponder("availability.id@test.com");

        Long availabilityId = createAvailability(
                responderId,
                LocalDateTime.of(2026, 9, 2, 8, 0),
                LocalDateTime.of(2026, 9, 2, 16, 0)
        );

        mockMvc.perform(get(
                "/api/responder-availability/{id}",
                availabilityId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(availabilityId))
                .andExpect(jsonPath("$.responderId").value(responderId))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));
    }


    @Test
    void shouldGetAvailabilityByResponderId() throws Exception {

        Long responderId = createResponder("availability.responder@test.com");

        createAvailability(
                responderId,
                LocalDateTime.of(2026, 9, 3, 8, 0),
                LocalDateTime.of(2026, 9, 3, 12, 0)
        );

        createAvailability(
                responderId,
                LocalDateTime.of(2026, 9, 3, 13, 0),
                LocalDateTime.of(2026, 9, 3, 17, 0)
        );

        mockMvc.perform(get(
                "/api/responder-availability/responder/{responderId}",
                responderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[*].responderId", hasItem(
                        responderId.intValue()
                )));
    }


    @Test
    void shouldGetAllResponderAvailability() throws Exception {

        Long responderOne = createResponder("availability.all1@test.com");
        Long responderTwo = createResponder("availability.all2@test.com");

        Long availabilityOne = createAvailability(
                responderOne,
                LocalDateTime.of(2026, 9, 4, 8, 0),
                LocalDateTime.of(2026, 9, 4, 12, 0)
        );

        Long availabilityTwo = createAvailability(
                responderTwo,
                LocalDateTime.of(2026, 9, 4, 13, 0),
                LocalDateTime.of(2026, 9, 4, 17, 0)
        );

        mockMvc.perform(get("/api/responder-availability"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[*].id", hasItem(
                        availabilityOne.intValue()
                )))
                .andExpect(jsonPath("$[*].id", hasItem(
                        availabilityTwo.intValue()
                )));
    }


    @Test
    void shouldUpdateResponderAvailability() throws Exception {

        Long responderId = createResponder("availability.update@test.com");

        Long availabilityId = createAvailability(
                responderId,
                LocalDateTime.of(2026, 9, 5, 8, 0),
                LocalDateTime.of(2026, 9, 5, 16, 0)
        );

        UpdateResponderAvailabilityRequestDto dto =
                new UpdateResponderAvailabilityRequestDto();

        dto.setAvailableFrom(LocalDateTime.of(2026, 9, 5, 10, 0));
        dto.setAvailableUntil(LocalDateTime.of(2026, 9, 5, 18, 0));
        dto.setStatus(AvailabilityStatus.UNAVAILABLE);

        mockMvc.perform(put(
                "/api/responder-availability/{id}",
                availabilityId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(availabilityId))
                .andExpect(jsonPath("$.responderId").value(responderId))
                .andExpect(jsonPath("$.status").value("UNAVAILABLE"));

        ResponderAvailability saved =
                responderAvailabilityRepository
                        .findById(availabilityId)
                        .orElseThrow();

        assertEquals(
                LocalDateTime.of(2026, 9, 5, 10, 0),
                saved.getAvailableFrom()
        );

        assertEquals(
                LocalDateTime.of(2026, 9, 5, 18, 0),
                saved.getAvailableUntil()
        );

        assertEquals(
                AvailabilityStatus.UNAVAILABLE,
                saved.getStatus()
        );
    }


    @Test
    void shouldDeleteResponderAvailability() throws Exception {

        Long responderId = createResponder("availability.delete@test.com");

        Long availabilityId = createAvailability(
                responderId,
                LocalDateTime.of(2026, 9, 6, 8, 0),
                LocalDateTime.of(2026, 9, 6, 16, 0)
        );

        mockMvc.perform(delete(
                "/api/responder-availability/{id}",
                availabilityId))
                .andExpect(status().isNoContent());

        assertFalse(
                responderAvailabilityRepository.existsById(availabilityId)
        );
    }

    @Test
void shouldReturnNotFoundWhenAvailabilityDoesNotExist() throws Exception {

    mockMvc.perform(get(
            "/api/responder-availability/{id}",
            999999L))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Not Found"))
            .andExpect(jsonPath("$.message")
                    .value("Responder availability not found with id: 999999"))
            .andExpect(jsonPath("$.timestamp").exists());
}


@Test
void shouldReturnNotFoundWhenCreatingAvailabilityForNonexistentResponder()
        throws Exception {

    CreateResponderAvailabilityRequestDto dto =
            new CreateResponderAvailabilityRequestDto();

    dto.setAvailableFrom(LocalDateTime.of(2026, 9, 7, 8, 0));
    dto.setAvailableUntil(LocalDateTime.of(2026, 9, 7, 16, 0));

    mockMvc.perform(post(
            "/api/responder-availability/responder/{responderId}",
            999999L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Not Found"))
            .andExpect(jsonPath("$.message")
                    .value("Responder not found with id: 999999"))
            .andExpect(jsonPath("$.timestamp").exists());
}


@Test
void shouldReturnBadRequestForInvalidAvailabilityRequest() throws Exception {

    Long responderId =
            createResponder("availability.invalid@test.com");

    CreateResponderAvailabilityRequestDto dto =
            new CreateResponderAvailabilityRequestDto();

    // availableFrom intentionally missing
    dto.setAvailableUntil(LocalDateTime.of(2026, 9, 7, 16, 0));

    mockMvc.perform(post(
            "/api/responder-availability/responder/{responderId}",
            responderId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("Bad Request"))
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.timestamp").exists());
}

    private Long createResponder(String email) throws Exception {

        AdminCreateUserRequestDto dto = new AdminCreateUserRequestDto();

        dto.setFirstName("Integration");
        dto.setLastName("Responder");
        dto.setEmail(email);
        dto.setPhoneNumber("12345678");
        dto.setPassword("integrationPassword123");
        dto.setStatus(UserStatus.ACTIVE);
        dto.setRoles(Set.of(RoleType.RESPONDER));

        mockMvc.perform(post("/api/users/admin/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        return userRepository
                .findByEmail(email)
                .orElseThrow()
                .getId();
    }


    private Long createAvailability(
            Long responderId,
            LocalDateTime availableFrom,
            LocalDateTime availableUntil) throws Exception {

        CreateResponderAvailabilityRequestDto dto =
                new CreateResponderAvailabilityRequestDto();

        dto.setAvailableFrom(availableFrom);
        dto.setAvailableUntil(availableUntil);

        mockMvc.perform(post(
                "/api/responder-availability/responder/{responderId}",
                responderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        return responderAvailabilityRepository
                .findByResponderId(responderId)
                .stream()
                .filter(a ->
                        a.getAvailableFrom().equals(availableFrom)
                        && a.getAvailableUntil().equals(availableUntil))
                .findFirst()
                .orElseThrow()
                .getId();
    }
}