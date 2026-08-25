package com.cicconesoftware.tripsentinel.service.checkinmethod;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cicconesoftware.tripsentinel.dto.checkinmethod.CheckInMethodResponseDto;
import com.cicconesoftware.tripsentinel.entity.CheckInMethod;
import com.cicconesoftware.tripsentinel.entity.enums.CheckInMethodType;
import com.cicconesoftware.tripsentinel.mapper.checkinmethod.CheckInMethodMapper;
import com.cicconesoftware.tripsentinel.repository.CheckInMethodRepository;

class CheckInMethodServiceTest {

    @Mock
    private CheckInMethodRepository checkInMethodRepository;

    private CheckInMethodService checkInMethodService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        CheckInMethodMapper mapper = new CheckInMethodMapper();

        checkInMethodService =
                new CheckInMethodServiceImpl(
                        mapper,
                        checkInMethodRepository
                );
    }

    @Test
    void shouldGetCheckInMethodById() {
        // Arrange
        CheckInMethod method = new CheckInMethod();
        method.setName(CheckInMethodType.PHONE);

        when(checkInMethodRepository.findById(1L))
                .thenReturn(Optional.of(method));

        // Act
        CheckInMethodResponseDto result =
                checkInMethodService.getById(1L);

        // Assert
        assertEquals(CheckInMethodType.PHONE, result.getName());

        verify(checkInMethodRepository).findById(1L);
    }

    @Test
    void shouldGetCheckInMethodByName() {
        // Arrange
        CheckInMethod method = new CheckInMethod();
        method.setName(CheckInMethodType.PHONE);

        when(checkInMethodRepository.findByName(CheckInMethodType.PHONE))
                .thenReturn(Optional.of(method));

        // Act
        CheckInMethodResponseDto result =
                checkInMethodService.getByName(CheckInMethodType.PHONE);

        // Assert
        assertEquals(CheckInMethodType.PHONE, result.getName());

        verify(checkInMethodRepository)
                .findByName(CheckInMethodType.PHONE);
    }

    @Test
    void shouldGetAllCheckInMethods() {
        // Arrange
        CheckInMethod phone = new CheckInMethod();
        phone.setName(CheckInMethodType.PHONE);

        CheckInMethod sms = new CheckInMethod();
        sms.setName(CheckInMethodType.SMS);

        when(checkInMethodRepository.findAll())
                .thenReturn(List.of(phone, sms));

        // Act
        List<CheckInMethodResponseDto> result =
                checkInMethodService.getAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals(CheckInMethodType.PHONE, result.get(0).getName());
        assertEquals(CheckInMethodType.SMS, result.get(1).getName());

        verify(checkInMethodRepository).findAll();
    }
}