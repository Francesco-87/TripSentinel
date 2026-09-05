package com.cicconesoftware.tripsentinel.mapper.session;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.cicconesoftware.tripsentinel.dto.session.AdminCreateCheckInSessionRequestDto;
import com.cicconesoftware.tripsentinel.dto.session.CheckInSessionResponseDto;
import com.cicconesoftware.tripsentinel.dto.session.CreateCheckInSessionRequestDto;
import com.cicconesoftware.tripsentinel.dto.session.UpdateCheckInSessionRequestDto;
import com.cicconesoftware.tripsentinel.entity.CheckInSession;
import com.cicconesoftware.tripsentinel.mapper.checkinmethod.CheckInMethodMapper;


@Component
/** Maps between check in session entities and DTOs. */
public class CheckInSessionMapper {



    private final SessionEventMapper sessionEventMapper;
    private final CheckInMethodMapper checkInMethodMapper;

    public CheckInSessionMapper(
        CheckInMethodMapper checkInMethodMapper,
        SessionEventMapper sessionEventMapper){
            this.checkInMethodMapper = checkInMethodMapper;
            this.sessionEventMapper = sessionEventMapper;
        }


    /** Maps session details; customer, responder, methods, and status are assigned by the service. */
    public CheckInSession toCheckInSessionEntity(AdminCreateCheckInSessionRequestDto dto){
         if (dto == null) {
            return null;
        }
        CheckInSession checkInSession = new CheckInSession();        
       
        checkInSession.setStartAt(dto.getStartAt());
        checkInSession.setExpectedReturnAt(dto.getExpectedReturnAt());
        checkInSession.setLatestCheckInAt(dto.getLatestCheckInAt());
        checkInSession.setLocationDescription(dto.getLocationDescription());
        checkInSession.setImportantNotes(dto.getImportantNotes());
    
        return checkInSession;

    }

    /** Maps session details; customer, responder, methods, and status are assigned by the service. */
     public CheckInSession toCheckInSessionEntity(CreateCheckInSessionRequestDto dto){
         if (dto == null) {
            return null;
        }
        CheckInSession checkInSession = new CheckInSession();

        checkInSession.setStartAt(dto.getStartAt());
        checkInSession.setExpectedReturnAt(dto.getExpectedReturnAt());
        checkInSession.setLatestCheckInAt(dto.getLatestCheckInAt());
        checkInSession.setLocationDescription(dto.getLocationDescription());
        checkInSession.setImportantNotes(dto.getImportantNotes());

        return checkInSession;
    }

    /** Applies supplied scalar details without changing participants, methods, or status. */
     public void updateCheckInSession(
        UpdateCheckInSessionRequestDto dto,
        CheckInSession session) {

    if (dto.getStartAt() != null) {
        session.setStartAt(dto.getStartAt());
    }
    if (dto.getExpectedReturnAt() != null) {
        session.setExpectedReturnAt(dto.getExpectedReturnAt());
    }
    if (dto.getLatestCheckInAt() != null) {
        session.setLatestCheckInAt(dto.getLatestCheckInAt());
    }
    if (dto.getLocationDescription() != null) {
        session.setLocationDescription(dto.getLocationDescription());
    }
    if (dto.getImportantNotes() != null) {
        session.setImportantNotes(dto.getImportantNotes());
    }
    }


  public CheckInSessionResponseDto toCheckInSessionResponseDto(CheckInSession checkInSession){

    if (checkInSession == null) {
            return null;
        }
        return new CheckInSessionResponseDto(
        checkInSession.getId(),
        checkInSession.getCustomer().getId(),
        checkInSession.getResponder().getId(),
        checkInSession.getEvents()
                .stream()
                .map(sessionEventMapper::toSessionEventResponseDto)
                .collect(Collectors.toSet()),
        checkInSession.getCheckInMethods()
                .stream()
                .map(checkInMethodMapper::toCheckInMethodResponseDto)
                .collect(Collectors.toSet()),
        checkInSession.getStartAt(),
        checkInSession.getExpectedReturnAt(),
        checkInSession.getLatestCheckInAt(),
        checkInSession.getLocationDescription(),
        checkInSession.getImportantNotes(),
        checkInSession.getStatus(),
        checkInSession.getCreatedAt(),
        checkInSession.getUpdatedAt()
);

        }
    
}
