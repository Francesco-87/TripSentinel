package com.cicconesoftware.tripsentinel.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cicconesoftware.tripsentinel.dto.session.AdminCreateCheckInSessionRequestDto;
import com.cicconesoftware.tripsentinel.dto.session.CheckInSessionResponseDto;
import com.cicconesoftware.tripsentinel.dto.session.CreateCheckInSessionRequestDto;
import com.cicconesoftware.tripsentinel.dto.session.UpdateCheckInSessionRequestDto;
import com.cicconesoftware.tripsentinel.service.session.CheckInSessionService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/check-in-sessions")
@CrossOrigin(origins = "http://localhost:5173")
/** Exposes HTTP endpoints for check in session operations. */
public class CheckInSessionController {

    private final CheckInSessionService checkInSessionService;

    public CheckInSessionController(CheckInSessionService checkInSessionService) {
        this.checkInSessionService = checkInSessionService;
    }

    @GetMapping
    public List<CheckInSessionResponseDto> getAllCheckInSessions() {
        return checkInSessionService.getAll();
    }

    @GetMapping("/by-user/{userId}")
    public List<CheckInSessionResponseDto> getCheckInSessionsByUser(@PathVariable Long userId) {
        return checkInSessionService.getByUserId(userId);
    }

    @GetMapping("/by-responder/{responderId}")
    public List<CheckInSessionResponseDto> getCheckInSessionsByResponder(@PathVariable Long responderId) {
        return checkInSessionService.getByResponderId(responderId);
    }

    @GetMapping("/{id}")
    public CheckInSessionResponseDto getCheckInSessionById(@PathVariable Long id) {
        return checkInSessionService.getById(id);
    }

    @PostMapping("/create-admin")
    @ResponseStatus(HttpStatus.CREATED)
    public CheckInSessionResponseDto createAdminCheckInSession(@RequestBody @Valid AdminCreateCheckInSessionRequestDto dto) {
        
        return checkInSessionService.adminCreateCheckInSession(dto);
    }
    
    @PostMapping("/create-user/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CheckInSessionResponseDto createCheckInSessionForUser(@RequestBody @Valid CreateCheckInSessionRequestDto dto, @PathVariable Long userId) {
        
        return checkInSessionService.createCheckInSessionForUser(dto, userId);
    }

   @PutMapping("/{sessionId}")
   public CheckInSessionResponseDto updateCheckInSession(
        @RequestBody @Valid UpdateCheckInSessionRequestDto dto,
        @PathVariable Long sessionId) {

    return checkInSessionService.updateCheckInSession(dto, sessionId);
}
    
    @DeleteMapping("/{sessionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelCheckInSession(@PathVariable Long sessionId) {
        checkInSessionService.cancelCheckInSession(sessionId);
    }

}
