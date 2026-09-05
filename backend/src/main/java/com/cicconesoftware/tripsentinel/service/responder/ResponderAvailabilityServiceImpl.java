package com.cicconesoftware.tripsentinel.service.responder;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cicconesoftware.tripsentinel.dto.responder.CreateResponderAvailabilityRequestDto;
import com.cicconesoftware.tripsentinel.dto.responder.ResponderAvailabilityResponseDto;
import com.cicconesoftware.tripsentinel.dto.responder.UpdateResponderAvailabilityRequestDto;
import com.cicconesoftware.tripsentinel.entity.ResponderAvailability;
import com.cicconesoftware.tripsentinel.entity.User;
import com.cicconesoftware.tripsentinel.entity.enums.AvailabilityStatus;
import com.cicconesoftware.tripsentinel.entity.enums.RoleType;
import com.cicconesoftware.tripsentinel.entity.enums.UserStatus;
import com.cicconesoftware.tripsentinel.exception.BadRequestException;
import com.cicconesoftware.tripsentinel.exception.ResourceNotFoundException;
import com.cicconesoftware.tripsentinel.mapper.responder.ResponderAvailabilityMapper;
import com.cicconesoftware.tripsentinel.repository.ResponderAvailabilityRepository;
import com.cicconesoftware.tripsentinel.repository.UserRepository;


@Service
/** Implements the responder availability application operations. */
public class ResponderAvailabilityServiceImpl implements ResponderAvailabilityService {

    private final ResponderAvailabilityMapper mapper;
    private final ResponderAvailabilityRepository repository;
    private final UserRepository userRepository;

    public ResponderAvailabilityServiceImpl (ResponderAvailabilityMapper mapper, ResponderAvailabilityRepository repository, UserRepository userRepository ){

        this.mapper = mapper;
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponderAvailabilityResponseDto getById(Long id){

        ResponderAvailability availability = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Responder availability not found with id: " + id));

        return mapper.toResponderAvailabilityResponseDto(availability);
    }


    @Override
    public List<ResponderAvailabilityResponseDto> getByResponderId(Long responderId){

        List<ResponderAvailability> availabilities = repository.findByResponderId(responderId);

        return availabilities.stream()
            .map(mapper::toResponderAvailabilityResponseDto)
            .toList();
    }

    @Override
    public List<ResponderAvailabilityResponseDto> getAll(){
        
        return repository.findAll().stream()
            .map(mapper::toResponderAvailabilityResponseDto)
            .toList();
    }


   @Override
    public ResponderAvailabilityResponseDto create(
            Long responderId,
            CreateResponderAvailabilityRequestDto dto
    ) {

        User responder = userRepository.findById(responderId)
            .orElseThrow(() -> new ResourceNotFoundException("Responder not found with id: " + responderId));

        if (responder.getStatus() != UserStatus.ACTIVE) {
            throw new BadRequestException("Responder must be active");
        }
        if (responder.getRoles().stream().noneMatch(role -> role.getName() == RoleType.RESPONDER)) {
            throw new BadRequestException("User must have the RESPONDER role");
        }

        ResponderAvailability availability =
            mapper.toResponderAvailability(dto);

        ZoneId timeZone = parseTimeZone(dto.getTimeZone());
        Instant availableFrom = toInstant(dto.getAvailableFrom(), timeZone);
        Instant availableUntil = toInstant(dto.getAvailableUntil(), timeZone);
        validateTimeOrder(availableFrom, availableUntil);

        availability.setResponder(responder);
        availability.setStatus(AvailabilityStatus.AVAILABLE);
        availability.setAvailableFrom(availableFrom);
        availability.setAvailableUntil(availableUntil);
        availability.setTimeZone(timeZone.getId());

        availability = repository.save(availability);

        return mapper.toResponderAvailabilityResponseDto(availability);
    }

    @Override
    public ResponderAvailabilityResponseDto update(
        Long id,
        UpdateResponderAvailabilityRequestDto dto
    ){

        ResponderAvailability availability = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Responder availability not found with id: " + id));

       mapper.updateResponderAvailability(dto, availability);

        ZoneId timeZone = parseTimeZone(dto.getTimeZone());
        Instant availableFrom = toInstant(dto.getAvailableFrom(), timeZone);
        Instant availableUntil = toInstant(dto.getAvailableUntil(), timeZone);
        validateTimeOrder(availableFrom, availableUntil);
        availability.setAvailableFrom(availableFrom);
        availability.setAvailableUntil(availableUntil);
        availability.setTimeZone(timeZone.getId());

        availability = repository.save(availability);

        return mapper.toResponderAvailabilityResponseDto(availability);
    }

    @Override
    public void delete(Long id){

        ResponderAvailability availability = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Responder availability not found with id: " + id));

        repository.delete(availability);
    }

    private void validateTimeOrder(Instant availableFrom, Instant availableUntil) {
        if (!availableFrom.isBefore(availableUntil)) {
            throw new BadRequestException("Availability end time must be after its start time");
        }
    }

    private ZoneId parseTimeZone(String timeZone) {
        try {
            return ZoneId.of(timeZone);
        } catch (DateTimeException | NullPointerException exception) {
            throw new BadRequestException("Invalid time zone: " + timeZone);
        }
    }

    private Instant toInstant(LocalDateTime localTime, ZoneId timeZone) {
        List<ZoneOffset> validOffsets = timeZone.getRules().getValidOffsets(localTime);
        if (validOffsets.isEmpty()) {
            throw new BadRequestException(
                    "Local time does not exist in " + timeZone.getId() + " because of a daylight-saving change");
        }
        if (validOffsets.size() > 1) {
            throw new BadRequestException(
                    "Local time is ambiguous in " + timeZone.getId() + " because of a daylight-saving change");
        }
        return localTime.atOffset(validOffsets.getFirst()).toInstant();
    }
    
}
