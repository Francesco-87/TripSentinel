package com.cicconesoftware.tripsentinel.dto.role;

import com.cicconesoftware.tripsentinel.entity.enums.RoleType;

public class RoleResponseDto {

    private final Long id;
    private final RoleType name;

    public RoleResponseDto(Long id, RoleType name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public RoleType getName() {
        return name;
    }

    
}
