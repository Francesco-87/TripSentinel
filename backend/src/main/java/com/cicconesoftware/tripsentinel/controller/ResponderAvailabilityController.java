package com.cicconesoftware.tripsentinel.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cicconesoftware.tripsentinel.dto.responder.CreateResponderAvailabilityRequestDto;
import com.cicconesoftware.tripsentinel.dto.responder.ResponderAvailabilityResponseDto;
import com.cicconesoftware.tripsentinel.dto.responder.UpdateResponderAvailabilityRequestDto;
import com.cicconesoftware.tripsentinel.service.responder.ResponderAvailabilityService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/responder-availability")
@CrossOrigin(origins = "http://localhost:5173")
/** Exposes HTTP endpoints for responder availability operations. */
public class ResponderAvailabilityController {

    private final ResponderAvailabilityService responderAvailabilityService;

    public ResponderAvailabilityController(ResponderAvailabilityService responderAvailabilityService) {
        this.responderAvailabilityService = responderAvailabilityService;
    }

    @PostMapping("/responder/{responderId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponderAvailabilityResponseDto createResponderAvailability(@PathVariable Long responderId, @RequestBody @Valid CreateResponderAvailabilityRequestDto dto) {
   
        return responderAvailabilityService.create(responderId, dto);
    }
    

    @GetMapping
    public List<ResponderAvailabilityResponseDto> getAllResponderAvailability() {
        return responderAvailabilityService.getAll();
    }

    @GetMapping("/{id}")
    public ResponderAvailabilityResponseDto getResponderAvailabilityById(@PathVariable Long id) {
        return responderAvailabilityService.getById(id);
    }

    @GetMapping("/responder/{responderId}")
    public List<ResponderAvailabilityResponseDto> getByResponderId(@PathVariable Long responderId) {
        return responderAvailabilityService.getByResponderId(responderId);
    }

    @PutMapping("/{id}")
    public ResponderAvailabilityResponseDto updateResponderAvailability(@PathVariable Long id, @RequestBody @Valid UpdateResponderAvailabilityRequestDto dto) {
        
        return responderAvailabilityService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteResponderAvailability(@PathVariable Long id) {
        responderAvailabilityService.delete(id);
    } 
    
    
    
}
