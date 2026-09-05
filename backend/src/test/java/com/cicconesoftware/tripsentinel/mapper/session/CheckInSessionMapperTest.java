package com.cicconesoftware.tripsentinel.mapper.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.Instant;
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
import com.cicconesoftware.tripsentinel.entity.enums.CheckInMethodType;
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

        AdminCreateCheckInSessionRequestDto requestDto =
                new AdminCreateCheckInSessionRequestDto();

        requestDto.setStartAt(startAt);
        requestDto.setExpectedReturnAt(expectedReturnAt);
        requestDto.setLatestCheckInAt(latestCheckInAt);
        requestDto.setLocationDescription("Nordmarka");
        requestDto.setImportantNotes("Take warm clothes.");

        // Act
        CheckInSession session =
                checkInSessionMapper.toCheckInSessionEntity(requestDto);

        // Assert
        assertNull(session.getStartAt());
        assertNull(session.getExpectedReturnAt());
        assertNull(session.getLatestCheckInAt());
        assertEquals("Nordmarka", session.getLocationDescription());
        assertEquals("Take warm clothes.", session.getImportantNotes());
    }

    @Test
    void shouldReturnNullWhenAdminCreateCheckInSessionRequestDtoIsNull() {
        assertNull(
                checkInSessionMapper.toCheckInSessionEntity(
                        (AdminCreateCheckInSessionRequestDto) null));
    }

    @Test
    void shouldMapCreateCheckInSessionRequestDtoToEntity() {
        // Arrange
        LocalDateTime startAt = LocalDateTime.now();
        LocalDateTime expectedReturnAt = startAt.plusHours(5);
        LocalDateTime latestCheckInAt = startAt.plusHours(6);

        CreateCheckInSessionRequestDto requestDto =
                new CreateCheckInSessionRequestDto();

        requestDto.setStartAt(startAt);
        requestDto.setExpectedReturnAt(expectedReturnAt);
        requestDto.setLatestCheckInAt(latestCheckInAt);
        requestDto.setLocationDescription("Nordmarka");
        requestDto.setImportantNotes("Take warm clothes.");

        // Act
        CheckInSession session =
                checkInSessionMapper.toCheckInSessionEntity(requestDto);

        // Assert
        assertNull(session.getStartAt());
        assertNull(session.getExpectedReturnAt());
        assertNull(session.getLatestCheckInAt());
        assertEquals("Nordmarka", session.getLocationDescription());
        assertEquals("Take warm clothes.", session.getImportantNotes());
    }

    @Test
    void shouldReturnNullWhenCreateCheckInSessionRequestDtoIsNull() {
        assertNull(
                checkInSessionMapper.toCheckInSessionEntity(
                        (CreateCheckInSessionRequestDto) null));
    }

    @Test
    void shouldUpdateCheckInSessionFromUpdateRequestDto() {
        // Arrange
        LocalDateTime startAt = LocalDateTime.now();
        LocalDateTime expectedReturnAt = startAt.plusHours(5);
        LocalDateTime latestCheckInAt = startAt.plusHours(6);

        UpdateCheckInSessionRequestDto requestDto =
                new UpdateCheckInSessionRequestDto();

        requestDto.setStartAt(startAt);
        requestDto.setExpectedReturnAt(expectedReturnAt);
        requestDto.setLatestCheckInAt(latestCheckInAt);
        requestDto.setLocationDescription("Nordmarka");
        requestDto.setImportantNotes("Take warm clothes.");

        CheckInSession session = new CheckInSession();
        session.setStatus(SessionStatus.PLANNED);
        Instant originalStart = Instant.parse("2026-09-01T06:00:00Z");
        Instant originalReturn = Instant.parse("2026-09-01T14:00:00Z");
        Instant originalDeadline = Instant.parse("2026-09-01T15:00:00Z");
        session.setStartAt(originalStart);
        session.setExpectedReturnAt(originalReturn);
        session.setLatestCheckInAt(originalDeadline);

        // Act
        checkInSessionMapper.updateCheckInSession(requestDto, session);

        // Assert
        assertEquals(originalStart, session.getStartAt());
        assertEquals(originalReturn, session.getExpectedReturnAt());
        assertEquals(originalDeadline, session.getLatestCheckInAt());
        assertEquals("Nordmarka", session.getLocationDescription());
        assertEquals("Take warm clothes.", session.getImportantNotes());

        // Existing fields remain untouched
        assertEquals(SessionStatus.PLANNED, session.getStatus());
    }

    @Test
    void shouldFailWhenUpdateCheckInSessionRequestDtoIsNull() {
        // Arrange
        CheckInSession session = new CheckInSession();

        // Act and assert
        assertThrows(
                NullPointerException.class,
                () -> checkInSessionMapper.updateCheckInSession(null, session));
    }

    @Test
    void shouldMapCheckInSessionToResponseDto() {
        // Arrange
        Instant startAt = Instant.parse("2026-09-01T06:00:00Z");
        Instant expectedReturnAt = startAt.plusSeconds(5 * 60 * 60);
        Instant latestCheckInAt = startAt.plusSeconds(6 * 60 * 60);
        Instant createdAt = startAt.minusSeconds(24 * 60 * 60);
        Instant updatedAt = startAt;

        User customer = new User();
        User responder = new User();

        CheckInMethod checkInMethod = new CheckInMethod();
        checkInMethod.setName(CheckInMethodType.PHONE);

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
        session.setTimeZone("Europe/Oslo");
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
        assertEquals("Europe/Oslo", responseDto.getTimeZone());
        assertEquals("Nordmarka", responseDto.getLocationDescription());
        assertEquals("Take warm clothes.", responseDto.getImportantNotes());
        assertEquals(SessionStatus.PLANNED, responseDto.getStatus());
        assertEquals(createdAt, responseDto.getCreatedAt());
        assertEquals(updatedAt, responseDto.getUpdatedAt());

        assertEquals(1, responseDto.getEvents().size());
        SessionEventResponseDto eventDto =
                responseDto.getEvents().iterator().next();

        assertEquals(SessionEventType.CREATED, eventDto.getType());

        assertEquals(1, responseDto.getCheckInMethods().size());
        CheckInMethodResponseDto methodDto =
                responseDto.getCheckInMethods().iterator().next();

        assertEquals(CheckInMethodType.PHONE, methodDto.getName());
    }

    @Test
    void shouldReturnNullWhenCheckInSessionIsNull() {
        assertNull(checkInSessionMapper.toCheckInSessionResponseDto(null));
    }
}
