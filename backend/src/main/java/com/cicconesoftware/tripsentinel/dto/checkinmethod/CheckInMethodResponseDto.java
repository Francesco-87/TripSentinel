package com.cicconesoftware.tripsentinel.dto.checkinmethod;

import com.cicconesoftware.tripsentinel.entity.enums.CheckInMethodType;

public class CheckInMethodResponseDto {

    private final Long id;
    private final CheckInMethodType name;

    public CheckInMethodResponseDto(Long id, CheckInMethodType name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public CheckInMethodType getName() {
        return name;
    }
    
}
