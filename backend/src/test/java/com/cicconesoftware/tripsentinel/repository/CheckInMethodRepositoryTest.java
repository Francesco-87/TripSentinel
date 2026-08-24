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
void shouldFindCheckInMethodByName() {
    // Act
    CheckInMethod checkInMethod =
            checkInMethodRepository.findByName(CheckInMethodType.PHONE)
                    .orElse(null);

    // Assert
    assertThat(checkInMethod).isNotNull();
    assertThat(checkInMethod.getName())
            .isEqualTo(CheckInMethodType.PHONE);
}
    
}
