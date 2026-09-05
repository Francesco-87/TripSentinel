package com.cicconesoftware.tripsentinel.mapper.user;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.cicconesoftware.tripsentinel.dto.user.AdminCreateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.AdminPatchUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.AdminUpdateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.CreateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.UserResponseDto;
import com.cicconesoftware.tripsentinel.dto.user.UserUpdateProfileRequestDto;
import com.cicconesoftware.tripsentinel.entity.User;

@Component
/** Maps between user entities and DTOs. */
public class UserMapper {

    /** Maps administrator-controlled fields; role associations are resolved by the service. */
    public User toUserEntity(AdminCreateUserRequestDto userRequestDto) {
        if (userRequestDto == null) {
            return null;
        }

        User user = new User();

        // TODO(auth): Encode the password before any user is persisted.

        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setEmail(userRequestDto.getEmail());
        user.setPhoneNumber(userRequestDto.getPhoneNumber());
        user.setPasswordHash(userRequestDto.getPassword());
        user.setStatus(userRequestDto.getStatus());

        return user;
    }

    /** Maps self-registration fields; default role and status are assigned by the service. */
    public User toUserEntity(CreateUserRequestDto userRequestDto) {
        if (userRequestDto == null) {
            return null;
        }

        User user = new User();

        // TODO(auth): Encode the password before any user is persisted.

        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setEmail(userRequestDto.getEmail());
        user.setPhoneNumber(userRequestDto.getPhoneNumber());
        user.setPasswordHash(userRequestDto.getPassword());

        return user;
    }

    /** Replaces administrator-editable scalar fields; roles are handled by the service. */
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

    /** Replaces only profile fields available to a regular user. */
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

    /** Applies supplied administrator fields; a blank phone number is treated as omitted. */
    public void updateUserFromAdminPatchDto(
            AdminPatchUserRequestDto dto,
            User user) {

        if (dto == null || user == null) {
            return;
        }

        if (dto.getFirstName() != null) {
            user.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            user.setLastName(dto.getLastName());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getPhoneNumber() != null && !dto.getPhoneNumber().isBlank()) {
            user.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getStatus() != null) {
            user.setStatus(dto.getStatus());
        }
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
