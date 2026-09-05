package com.cicconesoftware.tripsentinel.mapper.responder;

import org.springframework.stereotype.Component;

import com.cicconesoftware.tripsentinel.dto.responder.CreateResponderAvailabilityRequestDto;
import com.cicconesoftware.tripsentinel.dto.responder.ResponderAvailabilityResponseDto;
import com.cicconesoftware.tripsentinel.dto.responder.UpdateResponderAvailabilityRequestDto;
import com.cicconesoftware.tripsentinel.entity.ResponderAvailability;

@Component
/** Maps between responder availability entities and DTOs. */
public class ResponderAvailabilityMapper {

    /** Maps the requested time range; owner and initial status are assigned by the service. */
    public ResponderAvailability toResponderAvailability(CreateResponderAvailabilityRequestDto dto) {
        if (dto == null) {
            return null;
        }
        ResponderAvailability entity = new ResponderAvailability();
       
        entity.setAvailableFrom(dto.getAvailableFrom());
        entity.setAvailableUntil(dto.getAvailableUntil());
        return entity;
    }


    /** Replaces the time range and status while preserving the owning responder. */
    public void updateResponderAvailability(
        UpdateResponderAvailabilityRequestDto dto,
        ResponderAvailability entity) {

         if (dto == null || entity == null) {
                return;
            }

            entity.setAvailableFrom(dto.getAvailableFrom());
            entity.setAvailableUntil(dto.getAvailableUntil());
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
                responderAvailability.getStatus(),
                responderAvailability.getCreatedAt(),   
                responderAvailability.getUpdatedAt()
        );
    }
    
}
