package com.cicconesoftware.tripsentinel.service.responder;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cicconesoftware.tripsentinel.dto.responder.CreateResponderAvailabilityRequestDto;
import com.cicconesoftware.tripsentinel.dto.responder.ResponderAvailabilityResponseDto;
import com.cicconesoftware.tripsentinel.dto.responder.UpdateResponderAvailabilityRequestDto;
import com.cicconesoftware.tripsentinel.entity.ResponderAvailability;
import com.cicconesoftware.tripsentinel.entity.User;
import com.cicconesoftware.tripsentinel.entity.enums.AvailabilityStatus;
import com.cicconesoftware.tripsentinel.exception.ResourceNotFoundException;
import com.cicconesoftware.tripsentinel.mapper.responder.ResponderAvailabilityMapper;
import com.cicconesoftware.tripsentinel.repository.ResponderAvailabilityRepository;
import com.cicconesoftware.tripsentinel.repository.UserRepository;


@Service
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

        ResponderAvailability availability =
            mapper.toResponderAvailability(dto);

        availability.setResponder(responder);
        availability.setStatus(AvailabilityStatus.AVAILABLE);

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

        availability = repository.save(availability);

        return mapper.toResponderAvailabilityResponseDto(availability);
    }

    @Override
    public void delete(Long id){

        ResponderAvailability availability = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Responder availability not found with id: " + id));

        repository.delete(availability);
    }
    
}
