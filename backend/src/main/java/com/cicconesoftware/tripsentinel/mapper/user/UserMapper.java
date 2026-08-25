package com.cicconesoftware.tripsentinel.mapper.user;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.cicconesoftware.tripsentinel.dto.user.AdminCreateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.AdminUpdateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.CreateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.UserResponseDto;
import com.cicconesoftware.tripsentinel.dto.user.UserUpdateProfileRequestDto;
import com.cicconesoftware.tripsentinel.entity.User;

@Component
public class UserMapper {

    public User toUserEntity(AdminCreateUserRequestDto userRequestDto) {
        if (userRequestDto == null) {
            return null;
        }

        User user = new User();

        // TODO: hash password when authentication/security is implemented

        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setEmail(userRequestDto.getEmail());
        user.setPhoneNumber(userRequestDto.getPhoneNumber());
        user.setPasswordHash(userRequestDto.getPassword());
        user.setStatus(userRequestDto.getStatus());

        return user;
    }

    public User toUserEntity(CreateUserRequestDto userRequestDto) {
        if (userRequestDto == null) {
            return null;
        }

        User user = new User();

        // TODO: hash password when authentication/security is implemented

        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setEmail(userRequestDto.getEmail());
        user.setPhoneNumber(userRequestDto.getPhoneNumber());
        user.setPasswordHash(userRequestDto.getPassword());

        return user;
    }

    public void updateUserFromAdminDto(
            AdminUpdateUserRequestDto dto,
            User user) {

        if (dto == null || user == null) {
            return;
        }

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setStatus(dto.getStatus());
    }

    public void updateUserFromUserDto(
            UserUpdateProfileRequestDto userRequestDto,
            User user) {

        if (userRequestDto == null || user == null) {
            return;
        }

        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setEmail(userRequestDto.getEmail());
        user.setPhoneNumber(userRequestDto.getPhoneNumber());
    }

    public UserResponseDto toUserResponseDto(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getStatus(),
                user.getRoles()
                        .stream()
                        .map(role -> role.getName())
                        .collect(Collectors.toSet()),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}