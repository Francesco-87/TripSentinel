package com.cicconesoftware.tripsentinel.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import com.cicconesoftware.tripsentinel.entity.ResponderAvailability;
import com.cicconesoftware.tripsentinel.entity.Role;
import com.cicconesoftware.tripsentinel.entity.User;
import com.cicconesoftware.tripsentinel.entity.enums.AvailabilityStatus;
import com.cicconesoftware.tripsentinel.entity.enums.RoleType;
import com.cicconesoftware.tripsentinel.entity.enums.UserStatus;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ResponderAvailablityRepositoryTest {

    @Autowired
    private ResponderAvailabilityRepository responderAvailabilityRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndFindResponderAvailability() {
        // Arrange
        // Create a new responder availability entity and set its properties
        ResponderAvailability responderAvailability = new ResponderAvailability();
        responderAvailability.setAvailableFrom(java.time.LocalDateTime.now());
        responderAvailability.setAvailableUntil(java.time.LocalDateTime.now().plusHours(2));
        responderAvailability.setStatus(AvailabilityStatus.AVAILABLE);
        responderAvailability.setCreatedAt(java.time.LocalDateTime.now());
        responderAvailability.setUpdatedAt(java.time.LocalDateTime.now());

        User responder = new User();
        responder.setFirstName("firstnametestuser");
        responder.setLastName("lastnametestuser");
        responder.setEmail("testuser@example.com");
        responder.setPasswordHash("password");
        responder.setStatus(UserStatus.ACTIVE);
        responder.setPhoneNumber("743-555-1234");

        
        Optional<Role> foundRole = roleRepository.findByName(RoleType.RESPONDER);
        responder.getRoles().add(foundRole.get());
        User savedResponder = userRepository.save(responder);
        responderAvailability.setResponder(savedResponder);

        // Act
        ResponderAvailability savedResponderAvailability = responderAvailabilityRepository.save(responderAvailability);
        ResponderAvailability foundResponderAvailability = responderAvailabilityRepository.findById(savedResponderAvailability.getId()).orElse(null);

        // Assert
        assertThat(foundResponderAvailability).isNotNull();
        assertThat(foundResponderAvailability.getAvailableFrom()).isEqualTo(responderAvailability.getAvailableFrom());
        assertThat(foundResponderAvailability.getAvailableUntil()).isEqualTo(responderAvailability.getAvailableUntil());
        assertThat(foundResponderAvailability.getStatus()).isEqualTo(responderAvailability.getStatus());
        assertThat(foundResponderAvailability.getResponder()).isNotNull();
        assertThat(foundResponderAvailability.getResponder().getId()).isEqualTo(savedResponder.getId());
        assertThat(foundResponderAvailability.getResponder().getEmail()).isEqualTo(savedResponder.getEmail());
    }
    
}
