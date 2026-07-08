package com.cicconesoftware.tripsentinel.repository;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import com.cicconesoftware.tripsentinel.entity.Role;
import com.cicconesoftware.tripsentinel.entity.User;
import com.cicconesoftware.tripsentinel.entity.enums.RoleType;
import com.cicconesoftware.tripsentinel.entity.enums.UserStatus;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Test
    void shouldSaveUserWithRole() {
        // Arrange
        // Create a new user entity and set its properties
        User user = new User();
        user.setFirstName("firstnametestuser");
        user.setLastName("lastnametestuser");
        user.setEmail("testuser@example.com");
        user.setPasswordHash("password");
        user.setStatus(UserStatus.ACTIVE);
        user.setPhoneNumber("743-555-1234");

        
        Optional<Role> foundRole = roleRepository.findByName(RoleType.ADMIN);
        user.getRoles().add(foundRole.get());

        // Act
        User savedUser = userRepository.save(user);
        User foundUser = userRepository.findById(savedUser.getId()).orElse(null);

        // Assert
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getFirstName()).isEqualTo("firstnametestuser");
        assertThat(foundUser.getLastName()).isEqualTo("lastnametestuser");
        assertThat(foundUser.getEmail()).isEqualTo("testuser@example.com");
        assertThat(foundUser.getPasswordHash()).isEqualTo("password");
        assertThat(foundUser.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(foundUser.getPhoneNumber()).isEqualTo("743-555-1234");
        
        assertThat(foundUser.getRoles()).hasSize(1);

        Role assignedRole = foundUser.getRoles().iterator().next();
        assertThat(assignedRole.getName()).isEqualTo(RoleType.ADMIN);
}
}
