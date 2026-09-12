package com.cicconesoftware.tripsentinel.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import com.cicconesoftware.tripsentinel.entity.User;
import com.cicconesoftware.tripsentinel.entity.enums.UserStatus;
import com.cicconesoftware.tripsentinel.exception.GlobalExceptionHandler;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DuplicateEmailIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldTranslateDatabaseDuplicateEmailIntoFriendlyConflict() {
        String email = "duplicate-" + UUID.randomUUID() + "@example.com";
        userRepository.saveAndFlush(newUser(email));

        // Bypass the service check to reproduce the database failure from competing writes.
        DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class,
                () -> userRepository.saveAndFlush(newUser(email)));

        // Do not perform more database operations in the failed transaction.
        var response = new GlobalExceptionHandler().handleDataIntegrityViolation(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(409);
        assertThat(response.getBody().getMessage()).isEqualTo("Email already in use");
    }

    private User newUser(String email) {
        User user = new User();
        user.setFirstName("Duplicate");
        user.setLastName("Email test");
        user.setEmail(email);
        user.setPasswordHash("test-only-placeholder");
        user.setStatus(UserStatus.ACTIVE);
        return user;
    }
}
