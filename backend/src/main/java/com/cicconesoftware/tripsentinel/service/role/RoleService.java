package com.cicconesoftware.tripsentinel.service.role;

import java.util.List;

import com.cicconesoftware.tripsentinel.dto.role.RoleResponseDto;
import com.cicconesoftware.tripsentinel.entity.enums.RoleType;

public interface RoleService {

    RoleResponseDto getById(Long id);

    RoleResponseDto getByName(RoleType name);

    List<RoleResponseDto> getAll();    
} 