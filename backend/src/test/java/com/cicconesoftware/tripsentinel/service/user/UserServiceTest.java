package com.cicconesoftware.tripsentinel.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
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
import com.cicconesoftware.tripsentinel.dto.user.AdminPatchUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.AdminUpdateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.CreateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.UserResponseDto;
import com.cicconesoftware.tripsentinel.dto.user.UserUpdateProfileRequestDto;
import com.cicconesoftware.tripsentinel.entity.Role;
import com.cicconesoftware.tripsentinel.entity.User;
import com.cicconesoftware.tripsentinel.entity.enums.RoleType;
import com.cicconesoftware.tripsentinel.entity.enums.UserStatus;
import com.cicconesoftware.tripsentinel.exception.ConflictException;
import com.cicconesoftware.tripsentinel.mapper.user.UserMapper;
import com.cicconesoftware.tripsentinel.repository.RoleRepository;
import com.cicconesoftware.tripsentinel.repository.CheckInSessionRepository;
import com.cicconesoftware.tripsentinel.repository.UserRepository;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private CheckInSessionRepository checkInSessionRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        UserMapper userMapper = new UserMapper();

        userService = new UserServiceImpl(
                userMapper,
                userRepository,
                roleRepository,
                checkInSessionRepository
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

        verify(userRepository)
                .findByEmail("john@test.com");
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
        List<UserResponseDto> result =
                userService.getAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals(
                "John",
                result.get(0).getFirstName()
        );
        assertEquals(
                "Jane",
                result.get(1).getFirstName()
        );

        verify(userRepository).findAll();
    }

    @Test
    void shouldCreateCustomerUser() {
        // Arrange
        CreateUserRequestDto dto =
                new CreateUserRequestDto();

        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john@test.com");
        dto.setPhoneNumber("12345678");
        dto.setPassword("VerySecurePassword");

        Role customerRole = new Role();
        customerRole.setName(RoleType.CUSTOMER);

        when(roleRepository.findByName(RoleType.CUSTOMER))
                .thenReturn(Optional.of(customerRole));

        when(userRepository.save(any(User.class)))
                .thenAnswer(
                        invocation -> invocation.getArgument(0)
                );

        // Act
        UserResponseDto result =
                userService.create(dto);

        // Assert
        assertEquals(
                "John",
                result.getFirstName()
        );
        assertEquals(
                "john@test.com",
                result.getEmail()
        );
        assertEquals(
                UserStatus.ACTIVE,
                result.getStatus()
        );
        assertEquals(
                Set.of(RoleType.CUSTOMER),
                result.getRoles()
        );

        verify(roleRepository)
                .findByName(RoleType.CUSTOMER);

        verify(userRepository)
                .save(any(User.class));
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

        when(userRepository.save(any(User.class)))
                .thenAnswer(
                        invocation -> invocation.getArgument(0)
                );

        // Act
        UserResponseDto result =
                userService.adminCreate(dto);

        // Assert
        assertEquals(
                "John",
                result.getFirstName()
        );
        assertEquals(
                "john@test.com",
                result.getEmail()
        );
        assertEquals(
                Set.of(RoleType.CUSTOMER),
                result.getRoles()
        );

        verify(roleRepository)
                .findByName(RoleType.CUSTOMER);

        verify(userRepository)
                .save(any(User.class));
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
        dto.setRoles(Set.of(RoleType.RESPONDER));

        Role responderRole = new Role();
        responderRole.setName(RoleType.RESPONDER);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(existingUser));

        when(roleRepository.findByName(RoleType.RESPONDER))
                .thenReturn(Optional.of(responderRole));

        when(userRepository.save(existingUser))
                .thenReturn(existingUser);

        // Act
        UserResponseDto result =
                userService.adminUpdate(1L, dto);

        // Assert
        assertEquals(
                "Jane",
                result.getFirstName()
        );
        assertEquals(
                "jane@test.com",
                result.getEmail()
        );
        assertEquals(
                UserStatus.INACTIVE,
                result.getStatus()
        );
        assertEquals(
                Set.of(RoleType.RESPONDER),
                result.getRoles()
        );

        verify(userRepository).findById(1L);

        verify(roleRepository)
                .findByName(RoleType.RESPONDER);

        verify(userRepository)
                .save(existingUser);
    }

    @Test
    void shouldAdminPatchUserWithoutChangingRoles() {
        // Arrange
        User existingUser = createUser();

        AdminPatchUserRequestDto dto =
                new AdminPatchUserRequestDto();

        dto.setStatus(UserStatus.INACTIVE);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(existingUser));

        when(userRepository.save(existingUser))
                .thenReturn(existingUser);

        // Act
        UserResponseDto result =
                userService.adminPatch(1L, dto);

        // Assert
        assertEquals(
                "John",
                result.getFirstName()
        );
        assertEquals(
                "Doe",
                result.getLastName()
        );
        assertEquals(
                "john@test.com",
                result.getEmail()
        );
        assertEquals(
                UserStatus.INACTIVE,
                result.getStatus()
        );
        assertEquals(
                Set.of(RoleType.CUSTOMER),
                result.getRoles()
        );

        verify(userRepository).findById(1L);

        verify(roleRepository, never())
                .findByName(any(RoleType.class));

        verify(userRepository)
                .save(existingUser);
    }

    @Test
    void shouldAdminPatchUserWithoutChangingRolesWhenRoleSetIsEmpty() {
        User existingUser = createUser();
        AdminPatchUserRequestDto dto = new AdminPatchUserRequestDto();
        dto.setRoles(Set.of());

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        UserResponseDto result = userService.adminPatch(1L, dto);

        assertEquals(Set.of(RoleType.CUSTOMER), result.getRoles());
        verify(roleRepository, never()).findByName(any(RoleType.class));
    }

    @Test
    void shouldAdminPatchUserRoles() {
        // Arrange
        User existingUser = createUser();

        AdminPatchUserRequestDto dto =
                new AdminPatchUserRequestDto();

        dto.setRoles(Set.of(RoleType.RESPONDER));

        Role responderRole = new Role();
        responderRole.setName(RoleType.RESPONDER);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(existingUser));

        when(roleRepository.findByName(RoleType.RESPONDER))
                .thenReturn(Optional.of(responderRole));

        when(userRepository.save(existingUser))
                .thenReturn(existingUser);

        // Act
        UserResponseDto result =
                userService.adminPatch(1L, dto);

        // Assert
        assertEquals(
                "John",
                result.getFirstName()
        );
        assertEquals(
                UserStatus.ACTIVE,
                result.getStatus()
        );
        assertEquals(
                Set.of(RoleType.RESPONDER),
                result.getRoles()
        );

        verify(userRepository).findById(1L);

        verify(roleRepository)
                .findByName(RoleType.RESPONDER);

        verify(userRepository)
                .save(existingUser);
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
        assertEquals(
                "Max",
                result.getFirstName()
        );
        assertEquals(
                "Mustermann",
                result.getLastName()
        );
        assertEquals(
                "max@test.com",
                result.getEmail()
        );
        assertEquals(
                "99999999",
                result.getPhoneNumber()
        );

        verify(userRepository).findById(1L);
        verify(userRepository)
                .save(existingUser);
    }

    @Test
    void shouldWarnWhenDeactivatingResponderWithOpenSession() {
        User existingUser = createUser();
        Role responderRole = new Role();
        responderRole.setName(RoleType.RESPONDER);
        existingUser.setRoles(Set.of(responderRole));

        AdminPatchUserRequestDto dto = new AdminPatchUserRequestDto();
        dto.setStatus(UserStatus.INACTIVE);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(checkInSessionRepository.existsByResponderIdAndStatusIn(eq(1L), anySet()))
                .thenReturn(true);

        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> userService.adminPatch(1L, dto));

        assertEquals(
                "Responder still has open check-in sessions; reassign or resolve them before deactivation",
                exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldWarnWhenRemovingCustomerRoleWithOpenSession() {
        User existingUser = createUser();
        AdminPatchUserRequestDto dto = new AdminPatchUserRequestDto();
        dto.setRoles(Set.of(RoleType.RESPONDER));

        Role responderRole = new Role();
        responderRole.setName(RoleType.RESPONDER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(roleRepository.findByName(RoleType.RESPONDER)).thenReturn(Optional.of(responderRole));
        when(checkInSessionRepository.existsByCustomerIdAndStatusIn(eq(1L), anySet()))
                .thenReturn(true);

        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> userService.adminPatch(1L, dto));

        assertEquals(
                "Customer role cannot be removed while the user has open check-in sessions",
                exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldRejectProfileUpdateForInactiveUser() {
        User existingUser = createUser();
        existingUser.setStatus(UserStatus.INACTIVE);
        UserUpdateProfileRequestDto dto = new UserUpdateProfileRequestDto();

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        assertThrows(ConflictException.class, () -> userService.userUpdate(1L, dto));
        verify(userRepository, never()).save(any(User.class));
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
