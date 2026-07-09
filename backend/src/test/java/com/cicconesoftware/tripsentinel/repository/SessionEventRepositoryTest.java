package com.cicconesoftware.tripsentinel.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import com.cicconesoftware.tripsentinel.entity.CheckInMethod;
import com.cicconesoftware.tripsentinel.entity.CheckInSession;
import com.cicconesoftware.tripsentinel.entity.Role;
import com.cicconesoftware.tripsentinel.entity.SessionEvent;
import com.cicconesoftware.tripsentinel.entity.User;
import com.cicconesoftware.tripsentinel.entity.enums.RoleType;
import com.cicconesoftware.tripsentinel.entity.enums.SessionEventType;
import com.cicconesoftware.tripsentinel.entity.enums.SessionStatus;
import com.cicconesoftware.tripsentinel.entity.enums.UserStatus;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SessionEventRepositoryTest {

    @Autowired
    private SessionEventRepository sessionEventRepository;

    @Autowired
    private CheckInSessionRepository checkInSessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CheckInMethodRepository checkInMethodRepository;

    @Test
    void shouldSaveAndFindSessionEvent() {

        // Arrange

        // Customer
        User customer = new User();
        customer.setFirstName("Customer");
        customer.setLastName("Test");
        customer.setEmail("customer@test.com");
        customer.setPasswordHash("password");
        customer.setStatus(UserStatus.ACTIVE);

        Optional<Role> customerRole = roleRepository.findByName(RoleType.CUSTOMER);
        customer.getRoles().add(customerRole.get());

        User savedCustomer = userRepository.save(customer);

        // Responder
        User responder = new User();
        responder.setFirstName("Responder");
        responder.setLastName("Test");
        responder.setEmail("responder@test.com");
        responder.setPasswordHash("password");
        responder.setStatus(UserStatus.ACTIVE);

        Optional<Role> responderRole = roleRepository.findByName(RoleType.RESPONDER);
        responder.getRoles().add(responderRole.get());

        User savedResponder = userRepository.save(responder);

        // Check-in method
        CheckInMethod phoneMethod =
                checkInMethodRepository.findById(1L).orElseThrow();

        // Session
        CheckInSession session = new CheckInSession();
        session.setCustomer(savedCustomer);
        session.setResponder(savedResponder);
        session.setStartAt(LocalDateTime.now());
        session.setExpectedReturnAt(LocalDateTime.now().plusHours(3));
        session.setLatestCheckInAt(LocalDateTime.now().plusHours(4));
        session.setLocationDescription("Nordmarka");
        session.setImportantNotes("Repository test");
        session.setStatus(SessionStatus.PLANNED);
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());

        session.getCheckInMethods().add(phoneMethod);

        CheckInSession savedSession = checkInSessionRepository.save(session);

        // Event
        SessionEvent event = new SessionEvent();
        event.setSession(savedSession);
        event.setEventType(SessionEventType.CREATED);
        event.setNote("Session created");
        event.setCreatedAt(LocalDateTime.now());

        // Act

        SessionEvent savedEvent = sessionEventRepository.save(event);

        SessionEvent foundEvent =
                sessionEventRepository.findById(savedEvent.getId()).orElse(null);

        // Assert

        assertThat(foundEvent).isNotNull();

        assertThat(foundEvent.getEventType())
                .isEqualTo(SessionEventType.CREATED);

        assertThat(foundEvent.getNote())
                .isEqualTo("Session created");

        assertThat(foundEvent.getSession()).isNotNull();

        assertThat(foundEvent.getSession().getId())
                .isEqualTo(savedSession.getId());

        assertThat(foundEvent.getSession().getLocationDescription())
                .isEqualTo("Nordmarka");
    }
}