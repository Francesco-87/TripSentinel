package com.cicconesoftware.tripsentinel.dto.checkinmethod;

public class CheckInMethodResponseDto {

    private final long id;
    private final String name;

    public CheckInMethodResponseDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
}
