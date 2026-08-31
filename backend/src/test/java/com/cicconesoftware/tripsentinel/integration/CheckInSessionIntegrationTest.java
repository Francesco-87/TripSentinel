package com.cicconesoftware.tripsentinel.integration;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.cicconesoftware.tripsentinel.dto.session.AdminCreateCheckInSessionRequestDto;
import com.cicconesoftware.tripsentinel.dto.session.CreateCheckInSessionRequestDto;
import com.cicconesoftware.tripsentinel.dto.session.UpdateCheckInSessionRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.AdminCreateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.CreateUserRequestDto;
import com.cicconesoftware.tripsentinel.entity.CheckInMethod;
import com.cicconesoftware.tripsentinel.entity.CheckInSession;
import com.cicconesoftware.tripsentinel.entity.enums.CheckInMethodType;
import com.cicconesoftware.tripsentinel.entity.enums.RoleType;
import com.cicconesoftware.tripsentinel.entity.enums.SessionStatus;
import com.cicconesoftware.tripsentinel.entity.enums.UserStatus;
import com.cicconesoftware.tripsentinel.repository.CheckInMethodRepository;
import com.cicconesoftware.tripsentinel.repository.CheckInSessionRepository;
import com.cicconesoftware.tripsentinel.repository.UserRepository;

import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class CheckInSessionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CheckInSessionRepository checkInSessionRepository;

    @Autowired
    private CheckInMethodRepository checkInMethodRepository;


    @Test
    void shouldCreateSessionForUser() throws Exception {

        Long customerId = createCustomer("session.customer@test.com");
        Long responderId = createResponder("session.responder@test.com");
        Long methodId = getPhoneMethodId();

        CreateCheckInSessionRequestDto dto = new CreateCheckInSessionRequestDto();
        dto.setResponderId(responderId);
        dto.setCheckInMethodIds(Set.of(methodId));
        dto.setStartAt(LocalDateTime.of(2026, 9, 10, 8, 0));
        dto.setExpectedReturnAt(LocalDateTime.of(2026, 9, 10, 12, 0));
        dto.setLatestCheckInAt(LocalDateTime.of(2026, 9, 10, 13, 0));
        dto.setLocationDescription("Nordmarka south");
        dto.setImportantNotes("Integration test");

        mockMvc.perform(post("/api/check-in-sessions/create-user/{userId}", customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value(customerId))
                .andExpect(jsonPath("$.responderId").value(responderId))
                .andExpect(jsonPath("$.status").value("PLANNED"))
                .andExpect(jsonPath("$.locationDescription").value("Nordmarka south"))
                .andExpect(jsonPath("$.importantNotes").value("Integration test"));

        var sessions = checkInSessionRepository.findByCustomerId(customerId);

        assertEquals(1, sessions.size());
        assertEquals(SessionStatus.PLANNED, sessions.get(0).getStatus());
        assertEquals(responderId, sessions.get(0).getResponder().getId());
        assertEquals(1, sessions.get(0).getCheckInMethods().size());
    }


    @Test
    void shouldCreateSessionAsAdmin() throws Exception {

        Long customerId = createCustomer("session.admin.customer@test.com");
        Long responderId = createResponder("session.admin.responder@test.com");
        Long methodId = getPhoneMethodId();

        AdminCreateCheckInSessionRequestDto dto =
                new AdminCreateCheckInSessionRequestDto();

        dto.setCustomerId(customerId);
        dto.setResponderId(responderId);
        dto.setCheckInMethodIds(Set.of(methodId));
        dto.setStartAt(LocalDateTime.of(2026, 9, 11, 8, 0));
        dto.setExpectedReturnAt(LocalDateTime.of(2026, 9, 11, 12, 0));
        dto.setLatestCheckInAt(LocalDateTime.of(2026, 9, 11, 13, 0));
        dto.setLocationDescription("Admin-created session");
        dto.setImportantNotes("Admin notes");

        mockMvc.perform(post("/api/check-in-sessions/create-admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value(customerId))
                .andExpect(jsonPath("$.responderId").value(responderId))
                .andExpect(jsonPath("$.status").value("PLANNED"));

        var sessions = checkInSessionRepository.findByCustomerId(customerId);

        assertEquals(1, sessions.size());
        assertEquals(SessionStatus.PLANNED, sessions.get(0).getStatus());
    }


    @Test
    void shouldGetSessionById() throws Exception {

        Long customerId = createCustomer("session.get.id.customer@test.com");
        Long responderId = createResponder("session.get.id.responder@test.com");

        Long sessionId = createSession(customerId, responderId);

        mockMvc.perform(get("/api/check-in-sessions/{id}", sessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sessionId))
                .andExpect(jsonPath("$.customerId").value(customerId))
                .andExpect(jsonPath("$.responderId").value(responderId))
                .andExpect(jsonPath("$.status").value("PLANNED"));
    }


    @Test
    void shouldGetSessionsByUser() throws Exception {

        Long customerId = createCustomer("session.by.user@test.com");
        Long responderId = createResponder("session.by.user.responder@test.com");

        Long sessionId = createSession(customerId, responderId);

        mockMvc.perform(get("/api/check-in-sessions/by-user/{userId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[*].id", hasItem(sessionId.intValue())));
    }


    @Test
    void shouldGetSessionsByResponder() throws Exception {

        Long customerId = createCustomer("session.by.responder.customer@test.com");
        Long responderId = createResponder("session.by.responder@test.com");

        Long sessionId = createSession(customerId, responderId);

        mockMvc.perform(get(
                "/api/check-in-sessions/by-responder/{responderId}",
                responderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[*].id", hasItem(sessionId.intValue())));
    }


    @Test
    void shouldGetAllSessions() throws Exception {

        Long customerOne = createCustomer("session.all.customer1@test.com");
        Long customerTwo = createCustomer("session.all.customer2@test.com");

        Long responder = createResponder("session.all.responder@test.com");

        Long sessionOne = createSession(customerOne, responder);
        Long sessionTwo = createSession(customerTwo, responder);

        mockMvc.perform(get("/api/check-in-sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[*].id", hasItem(sessionOne.intValue())))
                .andExpect(jsonPath("$[*].id", hasItem(sessionTwo.intValue())));
    }


    @Test
    void shouldUpdateCheckInSession() throws Exception {

        Long customerId = createCustomer("session.update.customer@test.com");
        Long responderId = createResponder("session.update.responder@test.com");

        Long sessionId = createSession(customerId, responderId);

        Long methodId = getPhoneMethodId();

        UpdateCheckInSessionRequestDto dto =
                new UpdateCheckInSessionRequestDto();

        dto.setResponderId(responderId);
        dto.setCheckInMethodIds(Set.of(methodId));
        dto.setStartAt(LocalDateTime.of(2026, 9, 20, 10, 0));
        dto.setExpectedReturnAt(LocalDateTime.of(2026, 9, 20, 15, 0));
        dto.setLatestCheckInAt(LocalDateTime.of(2026, 9, 20, 16, 0));
        dto.setLocationDescription("Updated location");
        dto.setImportantNotes("Updated notes");

        mockMvc.perform(put("/api/check-in-sessions/{sessionId}", sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sessionId))
                .andExpect(jsonPath("$.locationDescription").value("Updated location"))
                .andExpect(jsonPath("$.importantNotes").value("Updated notes"));

        CheckInSession savedSession =
                checkInSessionRepository.findById(sessionId).orElseThrow();

        assertEquals(
                LocalDateTime.of(2026, 9, 20, 10, 0),
                savedSession.getStartAt()
        );

        assertEquals(
                LocalDateTime.of(2026, 9, 20, 15, 0),
                savedSession.getExpectedReturnAt()
        );

        assertEquals(
                LocalDateTime.of(2026, 9, 20, 16, 0),
                savedSession.getLatestCheckInAt()
        );

        assertEquals("Updated location", savedSession.getLocationDescription());
        assertEquals("Updated notes", savedSession.getImportantNotes());

        // Updating the session should not change its status.
        assertEquals(SessionStatus.PLANNED, savedSession.getStatus());
    }


    @Test
    void shouldCancelCheckInSession() throws Exception {

        Long customerId = createCustomer("session.cancel.customer@test.com");
        Long responderId = createResponder("session.cancel.responder@test.com");

        Long sessionId = createSession(customerId, responderId);

        mockMvc.perform(delete("/api/check-in-sessions/{sessionId}", sessionId))
                .andExpect(status().isNoContent());

        CheckInSession savedSession =
                checkInSessionRepository.findById(sessionId).orElseThrow();

        assertEquals(SessionStatus.CANCELLED, savedSession.getStatus());
    }


    private Long createCustomer(String email) throws Exception {

        CreateUserRequestDto dto = new CreateUserRequestDto();

        dto.setFirstName("Integration");
        dto.setLastName("Customer");
        dto.setEmail(email);
        dto.setPhoneNumber("11111111");
        dto.setPassword("integrationPassword123");

        mockMvc.perform(post("/api/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        return userRepository.findByEmail(email)
                .orElseThrow()
                .getId();
    }


    private Long createResponder(String email) throws Exception {

        AdminCreateUserRequestDto dto =
                new AdminCreateUserRequestDto();

        dto.setFirstName("Integration");
        dto.setLastName("Responder");
        dto.setEmail(email);
        dto.setPhoneNumber("22222222");
        dto.setPassword("integrationPassword123");
        dto.setStatus(UserStatus.ACTIVE);
        dto.setRoles(Set.of(RoleType.RESPONDER));

        mockMvc.perform(post("/api/users/admin/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        return userRepository.findByEmail(email)
                .orElseThrow()
                .getId();
    }


    private Long createSession(
            Long customerId,
            Long responderId) throws Exception {

        Long methodId = getPhoneMethodId();

        CreateCheckInSessionRequestDto dto =
                new CreateCheckInSessionRequestDto();

        dto.setResponderId(responderId);
        dto.setCheckInMethodIds(Set.of(methodId));
        dto.setStartAt(LocalDateTime.of(2026, 9, 15, 8, 0));
        dto.setExpectedReturnAt(LocalDateTime.of(2026, 9, 15, 12, 0));
        dto.setLatestCheckInAt(LocalDateTime.of(2026, 9, 15, 13, 0));
        dto.setLocationDescription("Integration location");
        dto.setImportantNotes("Integration notes");

        mockMvc.perform(post(
                "/api/check-in-sessions/create-user/{userId}",
                customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        return checkInSessionRepository
                .findByCustomerId(customerId)
                .stream()
                .filter(session ->
                        session.getResponder().getId().equals(responderId))
                .findFirst()
                .orElseThrow()
                .getId();
    }


    private Long getPhoneMethodId() {

        CheckInMethod method = checkInMethodRepository
                .findByName(CheckInMethodType.PHONE)
                .orElseThrow();

        return method.getId();
    }
}