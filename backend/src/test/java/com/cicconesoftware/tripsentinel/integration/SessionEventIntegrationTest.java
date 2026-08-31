package com.cicconesoftware.tripsentinel.integration;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import com.cicconesoftware.tripsentinel.dto.session.CreateCheckInSessionRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.AdminCreateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.CreateUserRequestDto;
import com.cicconesoftware.tripsentinel.entity.CheckInMethod;
import com.cicconesoftware.tripsentinel.entity.CheckInSession;
import com.cicconesoftware.tripsentinel.entity.SessionEvent;
import com.cicconesoftware.tripsentinel.entity.enums.CheckInMethodType;
import com.cicconesoftware.tripsentinel.entity.enums.RoleType;
import com.cicconesoftware.tripsentinel.entity.enums.SessionEventType;
import com.cicconesoftware.tripsentinel.entity.enums.UserStatus;
import com.cicconesoftware.tripsentinel.repository.CheckInMethodRepository;
import com.cicconesoftware.tripsentinel.repository.CheckInSessionRepository;
import com.cicconesoftware.tripsentinel.repository.SessionEventRepository;
import com.cicconesoftware.tripsentinel.repository.UserRepository;

import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class SessionEventIntegrationTest {

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

    @Autowired
    private SessionEventRepository sessionEventRepository;


    @Test
    void shouldGetSessionEventById() throws Exception {

        Long sessionId = createSession(
                "event.id.customer@test.com",
                "event.id.responder@test.com"
        );

        Long eventId = createEvent(
                sessionId,
                SessionEventType.CREATED,
                "Session created"
        );

        mockMvc.perform(get("/api/session-events/{id}", eventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(eventId))
                .andExpect(jsonPath("$.type").value("CREATED"))
                .andExpect(jsonPath("$.note").value("Session created"))
                .andExpect(jsonPath("$.createdAt").exists());

        SessionEvent savedEvent =
                sessionEventRepository.findById(eventId).orElseThrow();

        assertEquals(
                SessionEventType.CREATED,
                savedEvent.getEventType()
        );

        assertEquals(
                "Session created",
                savedEvent.getNote()
        );
    }


    @Test
    void shouldGetEventsBySessionId() throws Exception {

        Long sessionId = createSession(
                "event.list.customer@test.com",
                "event.list.responder@test.com"
        );

        Long eventOneId = createEvent(
                sessionId,
                SessionEventType.CREATED,
                "Session created"
        );

        Long eventTwoId = createEvent(
                sessionId,
                SessionEventType.CHECK_IN_REMINDER_SENT,
                "Reminder sent"
        );

        mockMvc.perform(get(
                "/api/session-events/by-session/{sessionId}",
                sessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[*].id",
                        hasItem(eventOneId.intValue())))
                .andExpect(jsonPath("$[*].id",
                        hasItem(eventTwoId.intValue())))
                .andExpect(jsonPath("$[*].type",
                        hasItem("CREATED")))
                .andExpect(jsonPath("$[*].type",
                        hasItem("CHECK_IN_REMINDER_SENT")));

        assertEquals(
                2,
                sessionEventRepository.findBySessionId(sessionId).size()
        );
    }


    private Long createEvent(
            Long sessionId,
            SessionEventType type,
            String note) {

        CheckInSession session =
                checkInSessionRepository
                        .findById(sessionId)
                        .orElseThrow();

        SessionEvent event = new SessionEvent();

        event.setSession(session);
        event.setEventType(type);
        event.setNote(note);
        event.setCreatedAt(LocalDateTime.now());

        return sessionEventRepository
                .save(event)
                .getId();
    }


    private Long createSession(
            String customerEmail,
            String responderEmail) throws Exception {

        Long customerId = createCustomer(customerEmail);
        Long responderId = createResponder(responderEmail);
        Long methodId = getPhoneMethodId();

        CreateCheckInSessionRequestDto dto =
                new CreateCheckInSessionRequestDto();

        dto.setResponderId(responderId);
        dto.setCheckInMethodIds(Set.of(methodId));
        dto.setStartAt(LocalDateTime.of(2026, 9, 20, 8, 0));
        dto.setExpectedReturnAt(LocalDateTime.of(2026, 9, 20, 12, 0));
        dto.setLatestCheckInAt(LocalDateTime.of(2026, 9, 20, 13, 0));
        dto.setLocationDescription("Integration event location");
        dto.setImportantNotes("Integration event test");

        mockMvc.perform(post(
                "/api/check-in-sessions/create-user/{userId}",
                customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        return checkInSessionRepository
                .findByCustomerId(customerId)
                .stream()
                .findFirst()
                .orElseThrow()
                .getId();
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

        return userRepository
                .findByEmail(email)
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

        return userRepository
                .findByEmail(email)
                .orElseThrow()
                .getId();
    }


    private Long getPhoneMethodId() {

        CheckInMethod method =
                checkInMethodRepository
                        .findByName(CheckInMethodType.PHONE)
                        .orElseThrow();

        return method.getId();
    }
}