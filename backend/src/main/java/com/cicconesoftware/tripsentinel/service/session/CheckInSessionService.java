package com.cicconesoftware.tripsentinel.service.session;

import java.util.List;

import com.cicconesoftware.tripsentinel.dto.session.AdminCreateCheckInSessionRequestDto;
import com.cicconesoftware.tripsentinel.dto.session.CheckInSessionResponseDto;
import com.cicconesoftware.tripsentinel.dto.session.CreateCheckInSessionRequestDto;
import com.cicconesoftware.tripsentinel.dto.session.UpdateCheckInSessionRequestDto;

public interface CheckInSessionService {

    CheckInSessionResponseDto getById(Long id);
    List<CheckInSessionResponseDto> getByUserId(Long customerId);
    List<CheckInSessionResponseDto> getByResponderId(Long responderId);
    List<CheckInSessionResponseDto> getAll();



    CheckInSessionResponseDto adminCreateCheckInSession(AdminCreateCheckInSessionRequestDto dto);
    CheckInSessionResponseDto createCheckInSessionForUser(CreateCheckInSessionRequestDto dto, Long userId);
    CheckInSessionResponseDto updateCheckInSession(UpdateCheckInSessionRequestDto dto, Long sessionId);

    CheckInSessionResponseDto cancelCheckInSession(Long sessionId);
}
