package com.cicconesoftware.tripsentinel.mapper.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.cicconesoftware.tripsentinel.dto.checkinmethod.CheckInMethodResponseDto;
import com.cicconesoftware.tripsentinel.dto.session.AdminCreateCheckInSessionRequestDto;
import com.cicconesoftware.tripsentinel.dto.session.CheckInSessionResponseDto;
import com.cicconesoftware.tripsentinel.dto.session.CreateCheckInSessionRequestDto;
import com.cicconesoftware.tripsentinel.dto.session.SessionEventResponseDto;
import com.cicconesoftware.tripsentinel.dto.session.UpdateCheckInSessionRequestDto;
import com.cicconesoftware.tripsentinel.entity.CheckInMethod;
import com.cicconesoftware.tripsentinel.entity.CheckInSession;
import com.cicconesoftware.tripsentinel.entity.SessionEvent;
import com.cicconesoftware.tripsentinel.entity.User;
import com.cicconesoftware.tripsentinel.entity.enums.SessionEventType;
import com.cicconesoftware.tripsentinel.entity.enums.SessionStatus;
import com.cicconesoftware.tripsentinel.mapper.checkinmethod.CheckInMethodMapper;

class CheckInSessionMapperTest {

    private final CheckInSessionMapper checkInSessionMapper =
            new CheckInSessionMapper(
                    new CheckInMethodMapper(),
                    new SessionEventMapper());

    @Test
    void shouldMapAdminCreateCheckInSessionRequestDtoToEntity() {
        // Arrange
        LocalDateTime startAt = LocalDateTime.now();
        LocalDateTime expectedReturnAt = startAt.plusHours(5);
        LocalDateTime latestCheckInAt = startAt.plusHours(6);

        AdminCreateCheckInSessionRequestDto requestDto = new AdminCreateCheckInSessionRequestDto();
        requestDto.setStartAt(startAt);
        requestDto.setExpectedReturnAt(expectedReturnAt);
        requestDto.setLatestCheckInAt(latestCheckInAt);
        requestDto.setLocationDescription("Nordmarka");
        requestDto.setImportantNotes("Take warm clothes.");

        // Act
        CheckInSession session = checkInSessionMapper.toCheckInSessionEntity(requestDto);

        // Assert
        assertEquals(startAt, session.getStartAt());
        assertEquals(expectedReturnAt, session.getExpectedReturnAt());
        assertEquals(latestCheckInAt, session.getLatestCheckInAt());
        assertEquals("Nordmarka", session.getLocationDescription());
        assertEquals("Take warm clothes.", session.getImportantNotes());
    }

    @Test
    void shouldReturnNullWhenAdminCreateCheckInSessionRequestDtoIsNull() {
        assertNull(checkInSessionMapper.toCheckInSessionEntity((AdminCreateCheckInSessionRequestDto) null));
    }

    @Test
    void shouldMapCreateCheckInSessionRequestDtoToEntity() {
        // Arrange
        LocalDateTime startAt = LocalDateTime.now();
        LocalDateTime expectedReturnAt = startAt.plusHours(5);
        LocalDateTime latestCheckInAt = startAt.plusHours(6);

        CreateCheckInSessionRequestDto requestDto = new CreateCheckInSessionRequestDto();
        requestDto.setStartAt(startAt);
        requestDto.setExpectedReturnAt(expectedReturnAt);
        requestDto.setLatestCheckInAt(latestCheckInAt);
        requestDto.setLocationDescription("Nordmarka");
        requestDto.setImportantNotes("Take warm clothes.");

        // Act
        CheckInSession session = checkInSessionMapper.toCheckInSessionEntity(requestDto);

        // Assert
        assertEquals(startAt, session.getStartAt());
        assertEquals(expectedReturnAt, session.getExpectedReturnAt());
        assertEquals(latestCheckInAt, session.getLatestCheckInAt());
        assertEquals("Nordmarka", session.getLocationDescription());
        assertEquals("Take warm clothes.", session.getImportantNotes());
    }

    @Test
    void shouldReturnNullWhenCreateCheckInSessionRequestDtoIsNull() {
        assertNull(checkInSessionMapper.toCheckInSessionEntity((CreateCheckInSessionRequestDto) null));
    }

    @Test
    void shouldMapUpdateCheckInSessionRequestDtoToEntity() {
        // Arrange
        LocalDateTime startAt = LocalDateTime.now();
        LocalDateTime expectedReturnAt = startAt.plusHours(5);
        LocalDateTime latestCheckInAt = startAt.plusHours(6);

        UpdateCheckInSessionRequestDto requestDto = new UpdateCheckInSessionRequestDto();
        requestDto.setStartAt(startAt);
        requestDto.setExpectedReturnAt(expectedReturnAt);
        requestDto.setLatestCheckInAt(latestCheckInAt);
        requestDto.setLocationDescription("Nordmarka");
        requestDto.setImportantNotes("Take warm clothes.");

        // Act
        CheckInSession session = checkInSessionMapper.toCheckInSessionEntity(requestDto);

        // Assert
        assertEquals(startAt, session.getStartAt());
        assertEquals(expectedReturnAt, session.getExpectedReturnAt());
        assertEquals(latestCheckInAt, session.getLatestCheckInAt());
        assertEquals("Nordmarka", session.getLocationDescription());
        assertEquals("Take warm clothes.", session.getImportantNotes());
    }

    @Test
    void shouldReturnNullWhenUpdateCheckInSessionRequestDtoIsNull() {
        assertNull(checkInSessionMapper.toCheckInSessionEntity((UpdateCheckInSessionRequestDto) null));
    }

    @Test
    void shouldMapCheckInSessionToResponseDto() {
        // Arrange
        LocalDateTime startAt = LocalDateTime.now();
        LocalDateTime expectedReturnAt = startAt.plusHours(5);
        LocalDateTime latestCheckInAt = startAt.plusHours(6);
        LocalDateTime createdAt = startAt.minusDays(1);
        LocalDateTime updatedAt = startAt;

        User customer = new User();
        User responder = new User();

        CheckInMethod checkInMethod = new CheckInMethod();
        checkInMethod.setName("PHONE");

        SessionEvent sessionEvent = new SessionEvent();
        sessionEvent.setEventType(SessionEventType.CREATED);
        sessionEvent.setNote("Session created.");
        sessionEvent.setCreatedAt(createdAt);

        CheckInSession session = new CheckInSession();
        session.setCustomer(customer);
        session.setResponder(responder);
        session.setCheckInMethods(Set.of(checkInMethod));
        session.setEvents(Set.of(sessionEvent));
        session.setStartAt(startAt);
        session.setExpectedReturnAt(expectedReturnAt);
        session.setLatestCheckInAt(latestCheckInAt);
        session.setLocationDescription("Nordmarka");
        session.setImportantNotes("Take warm clothes.");
        session.setStatus(SessionStatus.PLANNED);
        session.setCreatedAt(createdAt);
        session.setUpdatedAt(updatedAt);

        // Act
        CheckInSessionResponseDto responseDto =
                checkInSessionMapper.toCheckInSessionResponseDto(session);

        // Assert
        assertNull(responseDto.getId());
        assertNull(responseDto.getCustomerId());
        assertNull(responseDto.getResponderId());

        assertEquals(startAt, responseDto.getStartAt());
        assertEquals(expectedReturnAt, responseDto.getExpectedReturnAt());
        assertEquals(latestCheckInAt, responseDto.getLatestCheckInAt());
        assertEquals("Nordmarka", responseDto.getLocationDescription());
        assertEquals("Take warm clothes.", responseDto.getImportantNotes());
        assertEquals(SessionStatus.PLANNED, responseDto.getStatus());
        assertEquals(createdAt, responseDto.getCreatedAt());
        assertEquals(updatedAt, responseDto.getUpdatedAt());

        assertEquals(1, responseDto.getEvents().size());
        SessionEventResponseDto eventDto = responseDto.getEvents().iterator().next();
        assertEquals(SessionEventType.CREATED, eventDto.getType());

        assertEquals(1, responseDto.getCheckInMethods().size());
        CheckInMethodResponseDto methodDto = responseDto.getCheckInMethods().iterator().next();
        assertEquals("PHONE", methodDto.getName());
    }

    @Test
    void shouldReturnNullWhenCheckInSessionIsNull() {
        assertNull(checkInSessionMapper.toCheckInSessionResponseDto(null));
    }
}