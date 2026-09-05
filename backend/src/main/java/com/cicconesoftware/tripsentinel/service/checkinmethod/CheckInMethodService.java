package com.cicconesoftware.tripsentinel.service.checkinmethod;

import java.util.List;

import com.cicconesoftware.tripsentinel.dto.checkinmethod.CheckInMethodResponseDto;
import com.cicconesoftware.tripsentinel.entity.enums.CheckInMethodType;

/** Defines operations for managing check in method data. */
public interface CheckInMethodService {

    /** Returns one check-in method or fails when the ID does not exist. */
    CheckInMethodResponseDto getById(Long id);

    /** Returns one check-in method or fails when the method name does not exist. */
    CheckInMethodResponseDto getByName(CheckInMethodType name);

    /** Returns all configured check-in methods; no result order is guaranteed. */
    List<CheckInMethodResponseDto> getAll();
}
