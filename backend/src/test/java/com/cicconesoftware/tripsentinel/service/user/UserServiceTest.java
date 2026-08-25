package com.cicconesoftware.tripsentinel.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cicconesoftware.tripsentinel.dto.user.AdminCreateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.AdminUpdateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.UserResponseDto;
import com.cicconesoftware.tripsentinel.dto.user.UserUpdateProfileRequestDto;
import com.cicconesoftware.tripsentinel.entity.Role;
import com.cicconesoftware.tripsentinel.entity.User;
import com.cicconesoftware.tripsentinel.entity.enums.RoleType;
import com.cicconesoftware.tripsentinel.entity.enums.UserStatus;
import com.cicconesoftware.tripsentinel.mapper.user.UserMapper;
import com.cicconesoftware.tripsentinel.repository.RoleRepository;
import com.cicconesoftware.tripsentinel.repository.UserRepository;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        UserMapper userMapper = new UserMapper();

        userService = new UserServiceImpl(
                userMapper,
                userRepository,
                roleRepository
        );
    }

    @Test
    void shouldGetUserById() {
        // Arrange
        User user = createUser();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        // Act
        UserResponseDto result = userService.getById(1L);

        // Assert
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());

        verify(userRepository).findById(1L);
    }

    @Test
    void shouldGetUserByEmail() {
        // Arrange
        User user = createUser();

        when(userRepository.findByEmail("john@test.com"))
                .thenReturn(Optional.of(user));

        // Act
        UserResponseDto result =
                userService.getByEmail("john@test.com");

        // Assert
        assertEquals("john@test.com", result.getEmail());

        verify(userRepository).findByEmail("john@test.com");
    }

    @Test
    void shouldGetAllUsers() {
        // Arrange
        User first = createUser();

        User second = createUser();
        second.setFirstName("Jane");
        second.setEmail("jane@test.com");

        when(userRepository.findAll())
                .thenReturn(List.of(first, second));

        // Act
        List<UserResponseDto> result = userService.getAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());

        verify(userRepository).findAll();
    }

    @Test
    void shouldAdminCreateUser() {
        // Arrange
        AdminCreateUserRequestDto dto =
                new AdminCreateUserRequestDto();

        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john@test.com");
        dto.setPhoneNumber("12345678");
        dto.setPassword("VerySecurePassword");
        dto.setStatus(UserStatus.ACTIVE);
        dto.setRoles(Set.of(RoleType.CUSTOMER));

        Role customerRole = new Role();
        customerRole.setName(RoleType.CUSTOMER);

        when(roleRepository.findByName(RoleType.CUSTOMER))
                .thenReturn(Optional.of(customerRole));

        when(userRepository.save(
                org.mockito.ArgumentMatchers.any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserResponseDto result = userService.adminCreate(dto);

        // Assert
        assertEquals("John", result.getFirstName());
        assertEquals("john@test.com", result.getEmail());
        assertEquals(Set.of(RoleType.CUSTOMER), result.getRoles());

        verify(roleRepository).findByName(RoleType.CUSTOMER);
        verify(userRepository).save(
                org.mockito.ArgumentMatchers.any(User.class));
    }

    @Test
    void shouldAdminUpdateUser() {
        // Arrange
        User existingUser = createUser();

        AdminUpdateUserRequestDto dto =
                new AdminUpdateUserRequestDto();

        dto.setFirstName("Jane");
        dto.setLastName("Doe");
        dto.setEmail("jane@test.com");
        dto.setPhoneNumber("87654321");
        dto.setStatus(UserStatus.INACTIVE);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(existingUser));

        when(userRepository.save(existingUser))
                .thenReturn(existingUser);

        // Act
        UserResponseDto result =
                userService.adminUpdate(1L, dto);

        // Assert
        assertEquals("Jane", result.getFirstName());
        assertEquals("jane@test.com", result.getEmail());
        assertEquals(UserStatus.INACTIVE, result.getStatus());

        verify(userRepository).findById(1L);
        verify(userRepository).save(existingUser);
    }

    @Test
    void shouldUserUpdateProfile() {
        // Arrange
        User existingUser = createUser();

        UserUpdateProfileRequestDto dto =
                new UserUpdateProfileRequestDto();

        dto.setFirstName("Max");
        dto.setLastName("Mustermann");
        dto.setEmail("max@test.com");
        dto.setPhoneNumber("99999999");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(existingUser));

        when(userRepository.save(existingUser))
                .thenReturn(existingUser);

        // Act
        UserResponseDto result =
                userService.userUpdate(1L, dto);

        // Assert
        assertEquals("Max", result.getFirstName());
        assertEquals("Mustermann", result.getLastName());
        assertEquals("max@test.com", result.getEmail());
        assertEquals("99999999", result.getPhoneNumber());

        verify(userRepository).findById(1L);
        verify(userRepository).save(existingUser);
    }

    private User createUser() {
        Role customerRole = new Role();
        customerRole.setName(RoleType.CUSTOMER);

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@test.com");
        user.setPhoneNumber("12345678");
        user.setPasswordHash("VerySecurePassword");
        user.setStatus(UserStatus.ACTIVE);
        user.setRoles(Set.of(customerRole));

        return user;
    }
}