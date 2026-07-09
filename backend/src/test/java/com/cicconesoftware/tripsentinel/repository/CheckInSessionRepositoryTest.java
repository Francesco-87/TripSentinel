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
import com.cicconesoftware.tripsentinel.entity.User;
import com.cicconesoftware.tripsentinel.entity.enums.RoleType;
import com.cicconesoftware.tripsentinel.entity.enums.SessionStatus;
import com.cicconesoftware.tripsentinel.entity.enums.UserStatus;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CheckInSessionRepositoryTest {

    @Autowired
    private CheckInSessionRepository checkInSessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CheckInMethodRepository checkInMethodRepository;

    @Test
    void shouldSaveAndFindCheckInSession() {

        // Arrange

        // Create customer
        User customer = new User();
        customer.setFirstName("Customer");
        customer.setLastName("Test");
        customer.setEmail("customer@test.com");
        customer.setPasswordHash("password");
        customer.setStatus(UserStatus.ACTIVE);

        Optional<Role> customerRole = roleRepository.findByName(RoleType.CUSTOMER);
        customer.getRoles().add(customerRole.get());

        User savedCustomer = userRepository.save(customer);

        // Create responder
        User responder = new User();
        responder.setFirstName("Responder");
        responder.setLastName("Test");
        responder.setEmail("responder@test.com");
        responder.setPasswordHash("password");
        responder.setStatus(UserStatus.ACTIVE);

        Optional<Role> responderRole = roleRepository.findByName(RoleType.RESPONDER);
        responder.getRoles().add(responderRole.get());

        User savedResponder = userRepository.save(responder);

        // Retrieve seeded check-in method
        CheckInMethod phoneMethod =
                checkInMethodRepository.findById(1L).orElseThrow();

        // Create session
        CheckInSession session = new CheckInSession();
        session.setCustomer(savedCustomer);
        session.setResponder(savedResponder);
        session.setStartAt(LocalDateTime.now());
        session.setExpectedReturnAt(LocalDateTime.now().plusHours(3));
        session.setLatestCheckInAt(LocalDateTime.now().plusHours(4));
        session.setLocationDescription("Nordmarka");
        session.setImportantNotes("Test notes");
        session.setStatus(SessionStatus.PLANNED);
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());

        session.getCheckInMethods().add(phoneMethod);

        // Act

        CheckInSession savedSession =
                checkInSessionRepository.save(session);

        CheckInSession foundSession =
                checkInSessionRepository.findById(savedSession.getId()).orElse(null);

        // Assert

        assertThat(foundSession).isNotNull();

        assertThat(foundSession.getCustomer().getId())
                .isEqualTo(savedCustomer.getId());

        assertThat(foundSession.getResponder().getId())
                .isEqualTo(savedResponder.getId());

        assertThat(foundSession.getStatus())
                .isEqualTo(SessionStatus.PLANNED);

        assertThat(foundSession.getLocationDescription())
                .isEqualTo("Nordmarka");

        assertThat(foundSession.getImportantNotes())
                .isEqualTo("Test notes");

        assertThat(foundSession.getCheckInMethods())
                .hasSize(1);

        assertThat(foundSession.getCheckInMethods()
                .iterator()
                .next()
                .getName())
                .isEqualTo("PHONE");
    }
}