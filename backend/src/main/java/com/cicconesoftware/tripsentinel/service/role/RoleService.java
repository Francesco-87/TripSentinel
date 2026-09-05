package com.cicconesoftware.tripsentinel.service.role;

import java.util.List;

import com.cicconesoftware.tripsentinel.dto.role.RoleResponseDto;
import com.cicconesoftware.tripsentinel.entity.enums.RoleType;

/** Defines operations for managing role data. */
public interface RoleService {

    /** Returns one role or fails when the ID does not exist. */
    RoleResponseDto getById(Long id);

    /** Returns one role or fails when the role name does not exist. */
    RoleResponseDto getByName(RoleType name);

    /** Returns all configured roles; no result order is guaranteed. */
    List<RoleResponseDto> getAll();
}
