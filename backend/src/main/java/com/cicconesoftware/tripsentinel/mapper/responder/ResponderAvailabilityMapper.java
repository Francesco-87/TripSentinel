package com.cicconesoftware.tripsentinel.mapper.responder;

import org.springframework.stereotype.Component;

import com.cicconesoftware.tripsentinel.dto.responder.CreateResponderAvailabilityRequestDto;
import com.cicconesoftware.tripsentinel.dto.responder.ResponderAvailabilityResponseDto;
import com.cicconesoftware.tripsentinel.dto.responder.UpdateResponderAvailabilityRequestDto;
import com.cicconesoftware.tripsentinel.entity.ResponderAvailability;

@Component
public class ResponderAvailabilityMapper {

    public ResponderAvailability toResponderAvailability(CreateResponderAvailabilityRequestDto dto) {
          // Return null if input is null to prevent null pointer exceptions
        if (dto == null) {
            return null;
        }
          // Create new entity and map all DTO fields to corresponding entity fields
        ResponderAvailability entity = new ResponderAvailability();
       
        entity.setAvailableFrom(dto.getAvailableFrom());
        entity.setAvailableUntil(dto.getAvailableUntil());
        return entity;
    }


    public ResponderAvailability toResponderAvailability(UpdateResponderAvailabilityRequestDto dto) {
          // Return null if input is null to prevent null pointer exceptions
        if (dto == null) {
            return null;
        }
          // Create new entity and map all DTO fields to corresponding entity fields
        ResponderAvailability entity = new ResponderAvailability();
       
        entity.setAvailableFrom(dto.getAvailableFrom());
        entity.setAvailableUntil(dto.getAvailableUntil());
        entity.setStatus(dto.getStatus());
        return entity;
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
