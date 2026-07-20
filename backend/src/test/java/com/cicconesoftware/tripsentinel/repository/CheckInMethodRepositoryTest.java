package com.cicconesoftware.tripsentinel.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import com.cicconesoftware.tripsentinel.entity.CheckInMethod;
import com.cicconesoftware.tripsentinel.entity.enums.CheckInMethodType;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CheckInMethodRepositoryTest {

    @Autowired
    private CheckInMethodRepository checkInMethodRepository;

    @Test
    void shouldSaveAndFindCheckInMethod() {
        // Arrange
        // Create a new check-in method entity and set its properties
        CheckInMethod checkInMethod = new CheckInMethod();
        checkInMethod.setName(CheckInMethodType.PHONE);
        

        // Act
        CheckInMethod savedCheckInMethod = checkInMethodRepository.save(checkInMethod);
        CheckInMethod foundCheckInMethod = checkInMethodRepository.findById(savedCheckInMethod.getId()).orElse(null);

        // Assert
        assertThat(foundCheckInMethod).isNotNull();
        assertThat(foundCheckInMethod.getName()).isEqualTo(CheckInMethodType.PHONE);
    }
    
}
