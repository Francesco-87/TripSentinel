package com.cicconesoftware.tripsentinel.service.responder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cicconesoftware.tripsentinel.dto.responder.CreateResponderAvailabilityRequestDto;
import com.cicconesoftware.tripsentinel.dto.responder.ResponderAvailabilityResponseDto;
import com.cicconesoftware.tripsentinel.dto.responder.UpdateResponderAvailabilityRequestDto;
import com.cicconesoftware.tripsentinel.entity.ResponderAvailability;
import com.cicconesoftware.tripsentinel.entity.User;
import com.cicconesoftware.tripsentinel.entity.enums.AvailabilityStatus;
import com.cicconesoftware.tripsentinel.mapper.responder.ResponderAvailabilityMapper;
import com.cicconesoftware.tripsentinel.repository.ResponderAvailabilityRepository;
import com.cicconesoftware.tripsentinel.repository.UserRepository;

class ResponderAvailabilityServiceTest {

    @Mock
    private ResponderAvailabilityRepository repository;

    @Mock
    private UserRepository userRepository;

    private ResponderAvailabilityService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ResponderAvailabilityMapper mapper =
                new ResponderAvailabilityMapper();

        service = new ResponderAvailabilityServiceImpl(
                mapper,
                repository,
                userRepository
        );
    }

    @Test
    void shouldGetResponderAvailabilityById() {
        // Arrange
        User responder = new User();

        ResponderAvailability availability = new ResponderAvailability();
        availability.setResponder(responder);
        availability.setStatus(AvailabilityStatus.AVAILABLE);

        when(repository.findById(1L))
                .thenReturn(Optional.of(availability));

        // Act
        ResponderAvailabilityResponseDto result =
                service.getById(1L);

        // Assert
        assertEquals(
                AvailabilityStatus.AVAILABLE,
                result.getStatus()
        );

        verify(repository).findById(1L);
    }

    @Test
    void shouldGetResponderAvailabilityByResponderId() {
        // Arrange
        User responder = new User();

        ResponderAvailability first = new ResponderAvailability();
        first.setResponder(responder);
        first.setStatus(AvailabilityStatus.AVAILABLE);

        ResponderAvailability second = new ResponderAvailability();
        second.setResponder(responder);
        second.setStatus(AvailabilityStatus.UNAVAILABLE);

        when(repository.findByResponderId(1L))
                .thenReturn(List.of(first, second));

        // Act
        List<ResponderAvailabilityResponseDto> result =
                service.getByResponderId(1L);

        // Assert
        assertEquals(2, result.size());

        verify(repository).findByResponderId(1L);
    }

    @Test
    void shouldGetAllResponderAvailabilities() {
        // Arrange
        User responder = new User();

        ResponderAvailability first = new ResponderAvailability();
        first.setResponder(responder);

        ResponderAvailability second = new ResponderAvailability();
        second.setResponder(responder);

        when(repository.findAll())
                .thenReturn(List.of(first, second));

        // Act
        List<ResponderAvailabilityResponseDto> result =
                service.getAll();

        // Assert
        assertEquals(2, result.size());

        verify(repository).findAll();
    }

    @Test
    void shouldCreateResponderAvailability() {
        // Arrange
        LocalDateTime availableFrom = LocalDateTime.now();
        LocalDateTime availableUntil = availableFrom.plusHours(4);

        CreateResponderAvailabilityRequestDto dto =
                new CreateResponderAvailabilityRequestDto();

        dto.setAvailableFrom(availableFrom);
        dto.setAvailableUntil(availableUntil);

        User responder = new User();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(responder));

        when(repository.save(
                org.mockito.ArgumentMatchers.any(
                        ResponderAvailability.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponderAvailabilityResponseDto result =
                service.create(1L, dto);

        // Assert
        assertEquals(availableFrom, result.getAvailableFrom());
        assertEquals(availableUntil, result.getAvailableUntil());
        assertEquals(
                AvailabilityStatus.AVAILABLE,
                result.getStatus()
        );

        verify(userRepository).findById(1L);
        verify(repository).save(
                org.mockito.ArgumentMatchers.any(
                        ResponderAvailability.class));
    }

    @Test
    void shouldUpdateResponderAvailability() {
        // Arrange
        LocalDateTime availableFrom = LocalDateTime.now();
        LocalDateTime availableUntil = availableFrom.plusHours(6);

        UpdateResponderAvailabilityRequestDto dto =
                new UpdateResponderAvailabilityRequestDto();

        dto.setAvailableFrom(availableFrom);
        dto.setAvailableUntil(availableUntil);
        dto.setStatus(AvailabilityStatus.UNAVAILABLE);

        User responder = new User();

        ResponderAvailability existing =
                new ResponderAvailability();

        existing.setResponder(responder);
        existing.setStatus(AvailabilityStatus.AVAILABLE);

        when(repository.findById(1L))
                .thenReturn(Optional.of(existing));

        when(repository.save(existing))
                .thenReturn(existing);

        // Act
        ResponderAvailabilityResponseDto result =
                service.update(1L, dto);

        // Assert
        assertEquals(availableFrom, result.getAvailableFrom());
        assertEquals(availableUntil, result.getAvailableUntil());
        assertEquals(
                AvailabilityStatus.UNAVAILABLE,
                result.getStatus()
        );

        verify(repository).findById(1L);
        verify(repository).save(existing);
    }

    @Test
    void shouldDeleteResponderAvailability() {
        // Arrange
        ResponderAvailability availability =
                new ResponderAvailability();

        when(repository.findById(1L))
                .thenReturn(Optional.of(availability));

        // Act
        service.delete(1L);

        // Assert
        verify(repository).findById(1L);
        verify(repository).delete(availability);
    }
}