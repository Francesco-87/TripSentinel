package com.cicconesoftware.tripsentinel.controller;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cicconesoftware.tripsentinel.dto.checkinmethod.CheckInMethodResponseDto;
import com.cicconesoftware.tripsentinel.entity.enums.CheckInMethodType;
import com.cicconesoftware.tripsentinel.service.checkinmethod.CheckInMethodService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/check-in-methods")
// Allows requests from the frontend running on localhost:5173 to access these endpoints
@CrossOrigin(origins = "http://localhost:5173")
public class CheckInMethodController {

    private final CheckInMethodService checkInMethodService;

    public CheckInMethodController(CheckInMethodService checkInMethodService) {
        this.checkInMethodService = checkInMethodService;
    }

    @GetMapping
    public List<CheckInMethodResponseDto> getAllCheckInMethods() {
        return checkInMethodService.getAll();
    }

    @GetMapping("/{id}")
    public CheckInMethodResponseDto getCheckInMethodById(@PathVariable Long id) {
        return checkInMethodService.getById(id);
    }

    @GetMapping("/by-name/{name}")
    public CheckInMethodResponseDto getCheckInMethodByName(@PathVariable CheckInMethodType name) {   
        return checkInMethodService.getByName(name);
    }


    
}
