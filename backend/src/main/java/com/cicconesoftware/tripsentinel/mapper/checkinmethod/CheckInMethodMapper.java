package com.cicconesoftware.tripsentinel.mapper.checkinmethod;

import org.springframework.stereotype.Component;

import com.cicconesoftware.tripsentinel.dto.checkinmethod.CheckInMethodResponseDto;
import com.cicconesoftware.tripsentinel.entity.CheckInMethod;

@Component
public class CheckInMethodMapper {

    public CheckInMethodResponseDto toCheckInMethodResponseDto(CheckInMethod checkInMethod) {
        if (checkInMethod == null) {
            return null;
        }
        return new CheckInMethodResponseDto(
                checkInMethod.getId(),
                checkInMethod.getName()
        );
    }
    
}
