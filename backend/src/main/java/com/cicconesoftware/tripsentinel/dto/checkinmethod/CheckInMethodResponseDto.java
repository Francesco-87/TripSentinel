package com.cicconesoftware.tripsentinel.dto.checkinmethod;

public class CheckInMethodResponseDto {

    private final Long id;
    private final String name;

    public CheckInMethodResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
}
