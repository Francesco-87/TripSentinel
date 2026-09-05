package com.cicconesoftware.tripsentinel.service.responder;

import java.util.List;

import com.cicconesoftware.tripsentinel.dto.responder.CreateResponderAvailabilityRequestDto;
import com.cicconesoftware.tripsentinel.dto.responder.ResponderAvailabilityResponseDto;
import com.cicconesoftware.tripsentinel.dto.responder.UpdateResponderAvailabilityRequestDto;

/** Defines operations for managing responder availability data. */
public interface ResponderAvailabilityService {

    /** Returns one availability slot or fails when the ID does not exist. */
    ResponderAvailabilityResponseDto getById(Long id);

    /** Returns all availability slots; no result order is guaranteed. */
    List<ResponderAvailabilityResponseDto> getAll();

    /** Returns all availability slots belonging to a responder. */
    List<ResponderAvailabilityResponseDto> getByResponderId(Long responderId);

    /** Creates an available slot owned by the supplied responder. */
    ResponderAvailabilityResponseDto create(
        Long responderId,
        CreateResponderAvailabilityRequestDto dto
    );

    /** Replaces the time range and status of an availability slot. */
    ResponderAvailabilityResponseDto update(
        Long id,
        UpdateResponderAvailabilityRequestDto dto
    );

    /** Permanently deletes an availability slot. */
    void delete(Long id);
}
