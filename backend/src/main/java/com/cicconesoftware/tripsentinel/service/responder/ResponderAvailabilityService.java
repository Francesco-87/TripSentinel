package com.cicconesoftware.tripsentinel.service.responder;

import java.util.List;

import com.cicconesoftware.tripsentinel.dto.responder.CreateResponderAvailabilityRequestDto;
import com.cicconesoftware.tripsentinel.dto.responder.ResponderAvailabilityResponseDto;
import com.cicconesoftware.tripsentinel.dto.responder.UpdateResponderAvailabilityRequestDto;

public interface ResponderAvailabilityService {

ResponderAvailabilityResponseDto getById(Long id);

List<ResponderAvailabilityResponseDto> getAll();

List<ResponderAvailabilityResponseDto> getByResponderId(Long responderId);

ResponderAvailabilityResponseDto create(
    Long responderId,
    CreateResponderAvailabilityRequestDto dto
);

ResponderAvailabilityResponseDto update(
    Long id,
    UpdateResponderAvailabilityRequestDto dto
);

void delete(Long id);

    
}