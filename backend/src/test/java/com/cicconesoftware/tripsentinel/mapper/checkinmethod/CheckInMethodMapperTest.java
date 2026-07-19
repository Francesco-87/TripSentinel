package com.cicconesoftware.tripsentinel.mapper.checkinmethod;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.cicconesoftware.tripsentinel.dto.checkinmethod.CheckInMethodResponseDto;
import com.cicconesoftware.tripsentinel.entity.CheckInMethod;


class CheckInMethodMapperTest {

    private final CheckInMethodMapper checkInMethodMapper = new CheckInMethodMapper();

    @Test
    void shouldMapCheckInMethodToCheckInMethodResponseDto() {
        // Arrange
        CheckInMethod checkInMethod = new CheckInMethod();
        checkInMethod.setName("PHONE");

        // Act
        CheckInMethodResponseDto responseDto =
                checkInMethodMapper.toCheckInMethodResponseDto(checkInMethod);

        // Assert
        assertNull(responseDto.getId());
        assertEquals("PHONE", responseDto.getName());
    }

    @Test
    void shouldReturnNullWhenCheckInMethodIsNull() {
        // Act
        CheckInMethodResponseDto responseDto =
                checkInMethodMapper.toCheckInMethodResponseDto(null);

        // Assert
        assertNull(responseDto);
    }
}