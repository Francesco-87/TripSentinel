package com.cicconesoftware.tripsentinel.mapper.responder;

import org.springframework.stereotype.Component;

import com.cicconesoftware.tripsentinel.dto.responder.CreateResponderAvailabilityRequestDto;
import com.cicconesoftware.tripsentinel.dto.responder.ResponderAvailabilityResponseDto;
import com.cicconesoftware.tripsentinel.dto.responder.UpdateResponderAvailabilityRequestDto;
import com.cicconesoftware.tripsentinel.entity.ResponderAvailability;

@Component
/** Maps between responder availability entities and DTOs. */
public class ResponderAvailabilityMapper {

    /** Creates an availability shell; owner, status, timezone, and instants are assigned by the service. */
    public ResponderAvailability toResponderAvailability(CreateResponderAvailabilityRequestDto dto) {
        if (dto == null) {
            return null;
        }
        ResponderAvailability entity = new ResponderAvailability();
       
        return entity;
    }


    /** Replaces status while the service handles the timezone-aware time range. */
    public void updateResponderAvailability(
        UpdateResponderAvailabilityRequestDto dto,
        ResponderAvailability entity) {

         if (dto == null || entity == null) {
                return;
            }

            entity.setStatus(dto.getStatus());
    
        }




    public ResponderAvailabilityResponseDto toResponderAvailabilityResponseDto(ResponderAvailability responderAvailability) {
        if (responderAvailability == null) {
            return null;
        }
        return new ResponderAvailabilityResponseDto(
                responderAvailability.getId(),
                responderAvailability.getResponder().getId(),
                responderAvailability.getAvailableFrom(),
                responderAvailability.getAvailableUntil(),
                responderAvailability.getTimeZone(),
                responderAvailability.getStatus(),
                responderAvailability.getCreatedAt(),   
                responderAvailability.getUpdatedAt()
        );
    }
    
}
