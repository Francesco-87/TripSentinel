package com.cicconesoftware.tripsentinel.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cicconesoftware.tripsentinel.dto.user.AdminCreateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.AdminUpdateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.CreateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.UserResponseDto;
import com.cicconesoftware.tripsentinel.dto.user.UserUpdateProfileRequestDto;
import com.cicconesoftware.tripsentinel.entity.enums.RoleType;
import com.cicconesoftware.tripsentinel.entity.enums.UserStatus;
import com.cicconesoftware.tripsentinel.service.user.UserService;
import tools.jackson.databind.ObjectMapper;

class UserControllerTest {

    @Mock
    private UserService userService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        UserController userController =
                new UserController(userService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldCreateUser() throws Exception {
        // Arrange
        CreateUserRequestDto dto = new CreateUserRequestDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john@test.com");
        dto.setPhoneNumber("12345678");
        dto.setPassword("VerySecurePassword");

        UserResponseDto response = createResponseDto();

        when(userService.create(any(CreateUserRequestDto.class)))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.email").value("john@test.com"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        verify(userService)
                .create(any(CreateUserRequestDto.class));
    }

    @Test
    void shouldAdminCreateUser() throws Exception {
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

        UserResponseDto response = createResponseDto();

        when(userService.adminCreate(
                any(AdminCreateUserRequestDto.class)))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(post("/api/users/admin/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.email").value("john@test.com"));

        verify(userService)
                .adminCreate(any(AdminCreateUserRequestDto.class));
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        // Arrange
        UserResponseDto first = createResponseDto();

        UserResponseDto second = new UserResponseDto(
                2L,
                "Jane",
                "Doe",
                "jane@test.com",
                "87654321",
                UserStatus.ACTIVE,
                Set.of(RoleType.CUSTOMER),
                null,
                null
        );

        when(userService.getAll())
                .thenReturn(List.of(first, second));

        // Act + Assert
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));

        verify(userService).getAll();
    }

    @Test
    void shouldGetUserById() throws Exception {
        // Arrange
        UserResponseDto response = createResponseDto();

        when(userService.getById(1L))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.email").value("john@test.com"));

        verify(userService).getById(1L);
    }

    @Test
    void shouldGetUserByEmail() throws Exception {
        // Arrange
        UserResponseDto response = createResponseDto();

        when(userService.getByEmail("john@test.com"))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(get("/api/users/email")
                        .param("email", "john@test.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email")
                        .value("john@test.com"));

        verify(userService)
                .getByEmail("john@test.com");
    }

    @Test
    void shouldAdminUpdateUser() throws Exception {
        // Arrange
        AdminUpdateUserRequestDto dto =
                new AdminUpdateUserRequestDto();

        dto.setFirstName("Jane");
        dto.setLastName("Doe");
        dto.setEmail("jane@test.com");
        dto.setPhoneNumber("87654321");
        dto.setStatus(UserStatus.INACTIVE);
        dto.setRoles(Set.of(RoleType.CUSTOMER));

        UserResponseDto response = new UserResponseDto(
                1L,
                "Jane",
                "Doe",
                "jane@test.com",
                "87654321",
                UserStatus.INACTIVE,
                Set.of(RoleType.CUSTOMER),
                null,
                null
        );

        when(userService.adminUpdate(
                eq(1L),
                any(AdminUpdateUserRequestDto.class)))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(put("/api/users/admin/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.status").value("INACTIVE"));

        verify(userService).adminUpdate(
                eq(1L),
                any(AdminUpdateUserRequestDto.class)
        );
    }

    @Test
    void shouldUpdateUserProfile() throws Exception {
        // Arrange
        UserUpdateProfileRequestDto dto =
                new UserUpdateProfileRequestDto();

        dto.setFirstName("Max");
        dto.setLastName("Mustermann");
        dto.setEmail("max@test.com");
        dto.setPhoneNumber("99999999");

        UserResponseDto response = new UserResponseDto(
                1L,
                "Max",
                "Mustermann",
                "max@test.com",
                "99999999",
                UserStatus.ACTIVE,
                Set.of(RoleType.CUSTOMER),
                null,
                null
        );

        when(userService.userUpdate(
                eq(1L),
                any(UserUpdateProfileRequestDto.class)))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Max"))
                .andExpect(jsonPath("$.email").value("max@test.com"));

        verify(userService).userUpdate(
                eq(1L),
                any(UserUpdateProfileRequestDto.class)
        );
    }

    private UserResponseDto createResponseDto() {
        return new UserResponseDto(
                1L,
                "John",
                "Doe",
                "john@test.com",
                "12345678",
                UserStatus.ACTIVE,
                Set.of(RoleType.CUSTOMER),
                null,
                null
        );
    }
}