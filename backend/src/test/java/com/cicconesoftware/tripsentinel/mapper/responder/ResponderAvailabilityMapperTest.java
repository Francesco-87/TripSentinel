package com.cicconesoftware.tripsentinel.mapper.responder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import java.time.Instant;

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
    assertNull(responderAvailability.getAvailableFrom());
    assertNull(responderAvailability.getAvailableUntil());
}

    @Test
        void shouldUpdateResponderAvailabilityEntityFromUpdateRequestDto() {
        // Arrange
        LocalDateTime availableFrom = LocalDateTime.now();
        LocalDateTime availableUntil = availableFrom.plusHours(4);

        UpdateResponderAvailabilityRequestDto requestDto =
                new UpdateResponderAvailabilityRequestDto();
        requestDto.setAvailableFrom(availableFrom);
        requestDto.setAvailableUntil(availableUntil);
        requestDto.setStatus(AvailabilityStatus.UNAVAILABLE);

        ResponderAvailability responderAvailability =
                new ResponderAvailability();
        Instant originalFrom = Instant.parse("2026-09-01T06:00:00Z");
        Instant originalUntil = Instant.parse("2026-09-01T14:00:00Z");
        responderAvailability.setAvailableFrom(originalFrom);
        responderAvailability.setAvailableUntil(originalUntil);

        // Act
        responderAvailabilityMapper.updateResponderAvailability(
                requestDto,
                responderAvailability
        );

        // Assert
        assertEquals(originalFrom, responderAvailability.getAvailableFrom());
        assertEquals(originalUntil, responderAvailability.getAvailableUntil());
        assertEquals(
                AvailabilityStatus.UNAVAILABLE,
                responderAvailability.getStatus()
        );
        }

    @Test
    void shouldMapResponderAvailabilityToResponseDto() {
       // Arrange
        Instant availableFrom = Instant.parse("2026-09-01T06:00:00Z");
        Instant availableUntil = availableFrom.plusSeconds(6 * 60 * 60);
        Instant createdAt = availableFrom.minusSeconds(24 * 60 * 60);
        Instant updatedAt = availableFrom;

        User responder = new User();

        ResponderAvailability responderAvailability = new ResponderAvailability();
        responderAvailability.setResponder(responder);
        responderAvailability.setAvailableFrom(availableFrom);
        responderAvailability.setAvailableUntil(availableUntil);
        responderAvailability.setTimeZone("Europe/Oslo");
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
        assertEquals("Europe/Oslo", responseDto.getTimeZone());
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
