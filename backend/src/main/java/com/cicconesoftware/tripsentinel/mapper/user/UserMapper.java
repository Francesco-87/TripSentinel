package com.cicconesoftware.tripsentinel.mapper.user;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.cicconesoftware.tripsentinel.dto.user.AdminCreateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.AdminUpdateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.UserResponseDto;
import com.cicconesoftware.tripsentinel.dto.user.UserUpdateProfileRequestDto;
import com.cicconesoftware.tripsentinel.entity.Role;
import com.cicconesoftware.tripsentinel.entity.User;

@Component
public class UserMapper {

    public User toUserEntity(AdminCreateUserRequestDto userRequestDto) {
        if (userRequestDto == null) {
            return null;
        }
        User user = new User();
        
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setEmail(userRequestDto.getEmail());
        user.setPhoneNumber(userRequestDto.getPhoneNumber());
        user.setPasswordHash(userRequestDto.getPassword());
        user.setStatus(userRequestDto.getStatus());
        return user;
    }

        public User toUserEntity(AdminUpdateUserRequestDto userRequestDto) {
        if (userRequestDto == null) {
            return null;
        }
        User user = new User();
    
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setEmail(userRequestDto.getEmail());
        user.setPhoneNumber(userRequestDto.getPhoneNumber());
        user.setStatus(userRequestDto.getStatus());
       
        return user;
    }


    public User toUserEntity(UserUpdateProfileRequestDto userRequestDto) {
        if (userRequestDto == null) {
            return null;
        }
        User user = new User();
    
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setEmail(userRequestDto.getEmail());
        user.setPhoneNumber(userRequestDto.getPhoneNumber());

        return user;
    }



    public UserResponseDto toUserResponseDto(User user) {

          // Return null if entity is null to prevent null pointer exceptions
        if (user == null) {
            return null;
        }
        // Create response DTO with all entity fields, including audit timestamps and status
        return new UserResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getStatus(),
                user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet()),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
    
}
