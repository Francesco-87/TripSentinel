package com.cicconesoftware.tripsentinel.service.checkinmethod;

import java.util.List;

import com.cicconesoftware.tripsentinel.dto.checkinmethod.CheckInMethodResponseDto;
import com.cicconesoftware.tripsentinel.entity.enums.CheckInMethodType;

public interface CheckInMethodService {

     CheckInMethodResponseDto getById(Long id);

     CheckInMethodResponseDto getByName(CheckInMethodType name);

     List<CheckInMethodResponseDto> getAll(); 

    
}