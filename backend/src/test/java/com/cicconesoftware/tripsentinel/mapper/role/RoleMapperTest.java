package com.cicconesoftware.tripsentinel.mapper.role;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.cicconesoftware.tripsentinel.dto.role.RoleResponseDto;
import com.cicconesoftware.tripsentinel.entity.Role;
import com.cicconesoftware.tripsentinel.entity.enums.RoleType;

class RoleMapperTest {

    private final RoleMapper roleMapper = new RoleMapper();

    @Test
    void shouldMapRoleToRoleResponseDto() {
        // Arrange
        Role role = new Role();
        role.setName(RoleType.ADMIN);

        // Act
        RoleResponseDto responseDto = roleMapper.toRoleResponseDto(role);

        // Assert
        assertNull(responseDto.getId());
        assertEquals(RoleType.ADMIN, responseDto.getName());
    }

    @Test
    void shouldReturnNullWhenRoleIsNull() {
        // Act
        RoleResponseDto responseDto = roleMapper.toRoleResponseDto(null);

        // Assert
        assertNull(responseDto);
    }
}