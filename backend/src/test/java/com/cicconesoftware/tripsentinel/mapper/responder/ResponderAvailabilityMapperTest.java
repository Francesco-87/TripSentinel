package com.cicconesoftware.tripsentinel.mapper.responder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.cicconesoftware.tripsentinel.dto.responder.CreateResponderAvailabilityRequestDto;
import com.cicconesoftware.tripsentinel.dto.responder.ResponderAvailabilityResponseDto;
import com.cicconesoftware.tripsentinel.dto.responder.UpdateResponderAvailabilityRequestDto;
import com.cicconesoftware.tripsentinel.entity.ResponderAvailability;
import com.cicconesoftware.tripsentinel.entity.User;
import com.cicconesoftware.tripsentinel.entity.enums.AvailabilityStatus;

class ResponderAvailabilityMapperTest {

    private final ResponderAvailabilityMapper responderAvailabilityMapper =
            new ResponderAvailabilityMapper();

    @Test
void shouldMapCreateRequestDtoToResponderAvailabilityEntity() {
    // Arrange
    LocalDateTime availableFrom = LocalDateTime.now();
    LocalDateTime availableUntil = availableFrom.plusHours(8);

    CreateResponderAvailabilityRequestDto requestDto =
            new CreateResponderAvailabilityRequestDto();
    requestDto.setAvailableFrom(availableFrom);
    requestDto.setAvailableUntil(availableUntil);

    // Act
    ResponderAvailability responderAvailability =
            responderAvailabilityMapper.toResponderAvailability(requestDto);

    // Assert
    assertEquals(availableFrom, responderAvailability.getAvailableFrom());
    assertEquals(availableUntil, responderAvailability.getAvailableUntil());
}

    @Test
void shouldMapUpdateRequestDtoToResponderAvailabilityEntity() {
    // Arrange
    LocalDateTime availableFrom = LocalDateTime.now();
    LocalDateTime availableUntil = availableFrom.plusHours(4);

    UpdateResponderAvailabilityRequestDto requestDto =
            new UpdateResponderAvailabilityRequestDto();
    requestDto.setAvailableFrom(availableFrom);
    requestDto.setAvailableUntil(availableUntil);
    requestDto.setStatus(AvailabilityStatus.UNAVAILABLE);

    // Act
    ResponderAvailability responderAvailability =
            responderAvailabilityMapper.toResponderAvailability(requestDto);

    // Assert
    assertEquals(availableFrom, responderAvailability.getAvailableFrom());
    assertEquals(availableUntil, responderAvailability.getAvailableUntil());
    assertEquals(AvailabilityStatus.UNAVAILABLE, responderAvailability.getStatus());
}

    @Test
    void shouldMapResponderAvailabilityToResponseDto() {
       // Arrange
        LocalDateTime availableFrom = LocalDateTime.now();
        LocalDateTime availableUntil = availableFrom.plusHours(6);
        LocalDateTime createdAt = availableFrom.minusDays(1);
        LocalDateTime updatedAt = availableFrom;

        User responder = new User();

        ResponderAvailability responderAvailability = new ResponderAvailability();
        responderAvailability.setResponder(responder);
        responderAvailability.setAvailableFrom(availableFrom);
        responderAvailability.setAvailableUntil(availableUntil);
        responderAvailability.setStatus(AvailabilityStatus.AVAILABLE);
        responderAvailability.setCreatedAt(createdAt);
        responderAvailability.setUpdatedAt(updatedAt);

        // Act
        ResponderAvailabilityResponseDto responseDto =
                responderAvailabilityMapper.toResponderAvailabilityResponseDto(responderAvailability);

        // Assert
        assertNull(responseDto.getId());
        assertNull(responseDto.getResponderId());
        assertEquals(availableFrom, responseDto.getAvailableFrom());
        assertEquals(availableUntil, responseDto.getAvailableUntil());
        assertEquals(AvailabilityStatus.AVAILABLE, responseDto.getStatus());
        assertEquals(createdAt, responseDto.getCreatedAt());
        assertEquals(updatedAt, responseDto.getUpdatedAt());
    }

    @Test
    void shouldReturnNullWhenResponderAvailabilityIsNull() {
        // Act
        ResponderAvailabilityResponseDto responseDto =
                responderAvailabilityMapper.toResponderAvailabilityResponseDto(null);

        // Assert
        assertNull(responseDto);
    }
}