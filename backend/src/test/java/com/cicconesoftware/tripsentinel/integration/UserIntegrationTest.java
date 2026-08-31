package com.cicconesoftware.tripsentinel.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.cicconesoftware.tripsentinel.dto.user.AdminCreateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.AdminPatchUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.AdminUpdateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.CreateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.UserUpdateProfileRequestDto;
import com.cicconesoftware.tripsentinel.entity.User;
import com.cicconesoftware.tripsentinel.entity.enums.RoleType;
import com.cicconesoftware.tripsentinel.entity.enums.UserStatus;
import com.cicconesoftware.tripsentinel.repository.UserRepository;

import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;


    @Test
    void shouldCreateCustomerThroughApi() throws Exception {

        CreateUserRequestDto dto = new CreateUserRequestDto();
        dto.setFirstName("Test");
        dto.setLastName("Customer");
        dto.setEmail("customer.integration@test.com");
        dto.setPhoneNumber("12345678");
        dto.setPassword("integrationPassword123");

        mockMvc.perform(post("/api/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("Customer"))
                .andExpect(jsonPath("$.email").value("customer.integration@test.com"))
                .andExpect(jsonPath("$.phoneNumber").value("12345678"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.roles", hasItem("CUSTOMER")));

        User savedUser = userRepository
                .findByEmail("customer.integration@test.com")
                .orElseThrow();

        assertEquals("Test", savedUser.getFirstName());
        assertEquals("Customer", savedUser.getLastName());
        assertEquals("12345678", savedUser.getPhoneNumber());
        assertEquals(UserStatus.ACTIVE, savedUser.getStatus());

        assertTrue(
                savedUser.getRoles().stream()
                        .anyMatch(role -> role.getName() == RoleType.CUSTOMER)
        );
    }


    @Test
    void shouldCreateUserAsAdmin() throws Exception {

        AdminCreateUserRequestDto dto = new AdminCreateUserRequestDto();
        dto.setFirstName("Admin");
        dto.setLastName("Created");
        dto.setEmail("admin.create@test.com");
        dto.setPhoneNumber("11111111");
        dto.setPassword("adminPassword123");
        dto.setStatus(UserStatus.ACTIVE);
        dto.setRoles(Set.of(RoleType.CUSTOMER, RoleType.RESPONDER));

        mockMvc.perform(post("/api/users/admin/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Admin"))
                .andExpect(jsonPath("$.lastName").value("Created"))
                .andExpect(jsonPath("$.email").value("admin.create@test.com"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.roles", hasItem("CUSTOMER")))
                .andExpect(jsonPath("$.roles", hasItem("RESPONDER")));

        User savedUser = userRepository
                .findByEmail("admin.create@test.com")
                .orElseThrow();

        assertEquals(2, savedUser.getRoles().size());
    }


    @Test
    void shouldGetUserById() throws Exception {

        Long userId = createCustomer(
                "get.id@test.com",
                "Get",
                "ById"
        );

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.firstName").value("Get"))
                .andExpect(jsonPath("$.lastName").value("ById"))
                .andExpect(jsonPath("$.email").value("get.id@test.com"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.roles", hasItem("CUSTOMER")));
    }


    @Test
    void shouldGetUserByEmail() throws Exception {

        createCustomer(
                "get.email@test.com",
                "Get",
                "ByEmail"
        );

        mockMvc.perform(get("/api/users/email")
                .param("email", "get.email@test.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Get"))
                .andExpect(jsonPath("$.lastName").value("ByEmail"))
                .andExpect(jsonPath("$.email").value("get.email@test.com"));
    }


    @Test
    void shouldGetAllUsers() throws Exception {

        createCustomer(
                "all.one@test.com",
                "User",
                "One"
        );

        createCustomer(
                "all.two@test.com",
                "User",
                "Two"
        );

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[*].email", hasItem("all.one@test.com")))
                .andExpect(jsonPath("$[*].email", hasItem("all.two@test.com")));
    }


    @Test
    void shouldUpdateUserAsAdmin() throws Exception {

        Long userId = createCustomer(
                "admin.put@test.com",
                "Before",
                "Update"
        );

        AdminUpdateUserRequestDto dto = new AdminUpdateUserRequestDto();
        dto.setFirstName("After");
        dto.setLastName("AdminUpdate");
        dto.setEmail("admin.put.updated@test.com");
        dto.setPhoneNumber("22222222");
        dto.setStatus(UserStatus.INACTIVE);
        dto.setRoles(Set.of(RoleType.RESPONDER));

        mockMvc.perform(put("/api/users/admin/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.firstName").value("After"))
                .andExpect(jsonPath("$.lastName").value("AdminUpdate"))
                .andExpect(jsonPath("$.email").value("admin.put.updated@test.com"))
                .andExpect(jsonPath("$.phoneNumber").value("22222222"))
                .andExpect(jsonPath("$.status").value("INACTIVE"))
                .andExpect(jsonPath("$.roles", hasItem("RESPONDER")));

        User savedUser = userRepository.findById(userId).orElseThrow();

        assertEquals("After", savedUser.getFirstName());
        assertEquals("admin.put.updated@test.com", savedUser.getEmail());
        assertEquals(UserStatus.INACTIVE, savedUser.getStatus());
        assertEquals(1, savedUser.getRoles().size());

        assertTrue(
                savedUser.getRoles().stream()
                        .anyMatch(role -> role.getName() == RoleType.RESPONDER)
        );
    }


    @Test
    void shouldPatchOnlyProvidedUserFieldsAsAdmin() throws Exception {

        Long userId = createCustomer(
                "admin.patch@test.com",
                "Before",
                "Patch"
        );

        AdminPatchUserRequestDto dto = new AdminPatchUserRequestDto();
        dto.setFirstName("Patched");
        dto.setStatus(UserStatus.INACTIVE);

        mockMvc.perform(patch("/api/users/admin/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Patched"))
                .andExpect(jsonPath("$.lastName").value("Patch"))
                .andExpect(jsonPath("$.email").value("admin.patch@test.com"))
                .andExpect(jsonPath("$.status").value("INACTIVE"))
                .andExpect(jsonPath("$.roles", hasItem("CUSTOMER")));

        User savedUser = userRepository.findById(userId).orElseThrow();

        assertEquals("Patched", savedUser.getFirstName());
        assertEquals("Patch", savedUser.getLastName());
        assertEquals("admin.patch@test.com", savedUser.getEmail());
        assertEquals(UserStatus.INACTIVE, savedUser.getStatus());

        assertTrue(
                savedUser.getRoles().stream()
                        .anyMatch(role -> role.getName() == RoleType.CUSTOMER)
        );
    }


    @Test
    void shouldUpdateOwnUserProfile() throws Exception {

        Long userId = createCustomer(
                "profile.update@test.com",
                "Profile",
                "Before"
        );

        UserUpdateProfileRequestDto dto = new UserUpdateProfileRequestDto();
        dto.setFirstName("Profile");
        dto.setLastName("Updated");
        dto.setEmail("profile.updated@test.com");
        dto.setPhoneNumber("33333333");

        mockMvc.perform(put("/api/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.firstName").value("Profile"))
                .andExpect(jsonPath("$.lastName").value("Updated"))
                .andExpect(jsonPath("$.email").value("profile.updated@test.com"))
                .andExpect(jsonPath("$.phoneNumber").value("33333333"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.roles", hasItem("CUSTOMER")));

        User savedUser = userRepository.findById(userId).orElseThrow();

        assertEquals("Updated", savedUser.getLastName());
        assertEquals("profile.updated@test.com", savedUser.getEmail());
        assertEquals("33333333", savedUser.getPhoneNumber());

        assertEquals(UserStatus.ACTIVE, savedUser.getStatus());

        assertTrue(
                savedUser.getRoles().stream()
                        .anyMatch(role -> role.getName() == RoleType.CUSTOMER)
        );
    }


    private Long createCustomer(
            String email,
            String firstName,
            String lastName) throws Exception {

        CreateUserRequestDto dto = new CreateUserRequestDto();
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setEmail(email);
        dto.setPhoneNumber("99999999");
        dto.setPassword("integrationPassword123");

        mockMvc.perform(post("/api/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        return userRepository
                .findByEmail(email)
                .orElseThrow()
                .getId();
    }
}