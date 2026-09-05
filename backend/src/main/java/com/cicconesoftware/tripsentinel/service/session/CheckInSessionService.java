package com.cicconesoftware.tripsentinel.service.session;

import java.util.List;

import com.cicconesoftware.tripsentinel.dto.session.AdminCreateCheckInSessionRequestDto;
import com.cicconesoftware.tripsentinel.dto.session.CheckInSessionResponseDto;
import com.cicconesoftware.tripsentinel.dto.session.CreateCheckInSessionRequestDto;
import com.cicconesoftware.tripsentinel.dto.session.UpdateCheckInSessionRequestDto;

/** Defines operations for managing check in session data. */
public interface CheckInSessionService {

    /** Returns one check-in session or fails when the ID does not exist. */
    CheckInSessionResponseDto getById(Long id);

    /** Returns sessions where the given user is the customer. */
    List<CheckInSessionResponseDto> getByUserId(Long customerId);

    /** Returns sessions assigned to the given responder. */
    List<CheckInSessionResponseDto> getByResponderId(Long responderId);

    /** Returns all sessions; no result order is guaranteed. */
    List<CheckInSessionResponseDto> getAll();

    /** Creates a future planned session for distinct customer and responder role holders. */
    CheckInSessionResponseDto adminCreateCheckInSession(AdminCreateCheckInSessionRequestDto dto);

    /** Creates a future planned session with the supplied customer and a distinct responder. */
    CheckInSessionResponseDto createCheckInSessionForUser(CreateCheckInSessionRequestDto dto, Long userId);

    /** Applies supplied session details, including optional responder and method changes. */
    CheckInSessionResponseDto updateCheckInSession(UpdateCheckInSessionRequestDto dto, Long sessionId);

    /** Cancels a session by changing its status; the record is preserved. */
    CheckInSessionResponseDto cancelCheckInSession(Long sessionId);
}
