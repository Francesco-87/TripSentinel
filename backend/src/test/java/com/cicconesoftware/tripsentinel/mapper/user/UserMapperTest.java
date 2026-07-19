package com.cicconesoftware.tripsentinel.mapper.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.cicconesoftware.tripsentinel.dto.user.AdminCreateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.AdminUpdateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.UserResponseDto;
import com.cicconesoftware.tripsentinel.dto.user.UserUpdateProfileRequestDto;
import com.cicconesoftware.tripsentinel.entity.Role;
import com.cicconesoftware.tripsentinel.entity.User;
import com.cicconesoftware.tripsentinel.entity.enums.RoleType;
import com.cicconesoftware.tripsentinel.entity.enums.UserStatus;

class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    void shouldMapAdminCreateUserRequestDtoToUserEntity() {
        // Arrange
        AdminCreateUserRequestDto requestDto = new AdminCreateUserRequestDto();
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");
        requestDto.setEmail("john.doe@test.com");
        requestDto.setPhoneNumber("12345678");
        requestDto.setPassword("VerySecurePassword");
        requestDto.setStatus(UserStatus.ACTIVE);

        // Act
        User user = userMapper.toUserEntity(requestDto);

        // Assert
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@test.com", user.getEmail());
        assertEquals("12345678", user.getPhoneNumber());
        assertEquals("VerySecurePassword", user.getPasswordHash());
        assertEquals(UserStatus.ACTIVE, user.getStatus());
    }

    @Test
    void shouldReturnNullWhenAdminCreateUserRequestDtoIsNull() {
        assertNull(userMapper.toUserEntity((AdminCreateUserRequestDto) null));
    }

    @Test
    void shouldMapAdminUpdateUserRequestDtoToUserEntity() {
        // Arrange
        AdminUpdateUserRequestDto requestDto = new AdminUpdateUserRequestDto();
        requestDto.setFirstName("Jane");
        requestDto.setLastName("Doe");
        requestDto.setEmail("jane.doe@test.com");
        requestDto.setPhoneNumber("87654321");
        requestDto.setStatus(UserStatus.INACTIVE);

        // Act
        User user = userMapper.toUserEntity(requestDto);

        // Assert
        assertEquals("Jane", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("jane.doe@test.com", user.getEmail());
        assertEquals("87654321", user.getPhoneNumber());
        assertEquals(UserStatus.INACTIVE, user.getStatus());
    }

    @Test
    void shouldReturnNullWhenAdminUpdateUserRequestDtoIsNull() {
        assertNull(userMapper.toUserEntity((AdminUpdateUserRequestDto) null));
    }

    @Test
    void shouldMapUserUpdateProfileRequestDtoToUserEntity() {
        // Arrange
        UserUpdateProfileRequestDto requestDto = new UserUpdateProfileRequestDto();
        requestDto.setFirstName("Max");
        requestDto.setLastName("Mustermann");
        requestDto.setEmail("max@test.com");
        requestDto.setPhoneNumber("99999999");

        // Act
        User user = userMapper.toUserEntity(requestDto);

        // Assert
        assertEquals("Max", user.getFirstName());
        assertEquals("Mustermann", user.getLastName());
        assertEquals("max@test.com", user.getEmail());
        assertEquals("99999999", user.getPhoneNumber());
    }

    @Test
    void shouldReturnNullWhenUserUpdateProfileRequestDtoIsNull() {
        assertNull(userMapper.toUserEntity((UserUpdateProfileRequestDto) null));
    }

    @Test
    void shouldMapUserToUserResponseDto() {
        // Arrange
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now();

        Role role = new Role();
        role.setName(RoleType.ADMIN);

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@test.com");
        user.setPhoneNumber("12345678");
        user.setStatus(UserStatus.ACTIVE);
        user.setRoles(roles);
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(updatedAt);

        // Act
        UserResponseDto responseDto = userMapper.toUserResponseDto(user);

        // Assert
        assertNull(responseDto.getId());
        assertEquals("John", responseDto.getFirstName());
        assertEquals("Doe", responseDto.getLastName());
        assertEquals("john.doe@test.com", responseDto.getEmail());
        assertEquals("12345678", responseDto.getPhoneNumber());
        assertEquals(UserStatus.ACTIVE, responseDto.getStatus());
        assertEquals(Set.of(RoleType.ADMIN), responseDto.getRoles());
        assertEquals(createdAt, responseDto.getCreatedAt());
        assertEquals(updatedAt, responseDto.getUpdatedAt());
    }

    @Test
    void shouldReturnNullWhenUserIsNull() {
        assertNull(userMapper.toUserResponseDto(null));
    }
}