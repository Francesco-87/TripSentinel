package com.cicconesoftware.tripsentinel.service.user;

import java.util.List;

import com.cicconesoftware.tripsentinel.dto.user.AdminCreateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.AdminUpdateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.UserResponseDto;
import com.cicconesoftware.tripsentinel.dto.user.UserUpdateProfileRequestDto;

public interface UserService {

    UserResponseDto getById(Long id);
    UserResponseDto getByEmail(String email);
    List<UserResponseDto> getAll();

    UserResponseDto adminCreate(AdminCreateUserRequestDto dto);
    UserResponseDto adminUpdate(Long id, AdminUpdateUserRequestDto dto);
    UserResponseDto userUpdate(Long id, UserUpdateProfileRequestDto dto);

    
}
