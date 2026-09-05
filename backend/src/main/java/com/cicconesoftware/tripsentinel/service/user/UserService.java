package com.cicconesoftware.tripsentinel.service.user;

import java.util.List;

import com.cicconesoftware.tripsentinel.dto.user.AdminCreateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.AdminPatchUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.AdminUpdateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.CreateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.UserResponseDto;
import com.cicconesoftware.tripsentinel.dto.user.UserUpdateProfileRequestDto;

/** Defines operations for managing user data. */
public interface UserService {

    /** Returns one user or fails when the ID does not exist. */
    UserResponseDto getById(Long id);

    /** Returns one user or fails when the email does not exist. */
    UserResponseDto getByEmail(String email);

    /** Returns all users; no result order is guaranteed. */
    List<UserResponseDto> getAll();

    /** Creates an active customer account; only administrators assign other roles. */
    UserResponseDto create(CreateUserRequestDto dto);

    /** Creates a user with administrator-supplied status and roles. */
    UserResponseDto adminCreate(AdminCreateUserRequestDto dto);

    /** Applies supplied administrator fields; empty roles and blank phone values are ignored. */
    UserResponseDto adminPatch(Long id, AdminPatchUserRequestDto dto);

    /** Replaces all administrator-managed user details and roles. */
    UserResponseDto adminUpdate(Long id, AdminUpdateUserRequestDto dto);

    /** Replaces the profile fields a regular user may manage. */
    UserResponseDto userUpdate(Long id, UserUpdateProfileRequestDto dto);
}
