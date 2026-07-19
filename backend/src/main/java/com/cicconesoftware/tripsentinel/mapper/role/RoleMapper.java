package com.cicconesoftware.tripsentinel.mapper.role;

import org.springframework.stereotype.Component;

import com.cicconesoftware.tripsentinel.dto.role.RoleResponseDto;
import com.cicconesoftware.tripsentinel.entity.Role;

@Component
public class RoleMapper {

    public RoleResponseDto toRoleResponseDto(Role role) {
        if (role == null) {
            return null;
        }
        return new RoleResponseDto(
                role.getId(),
                role.getName()
                
        );
    }
    
}
