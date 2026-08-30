package com.cicconesoftware.tripsentinel.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cicconesoftware.tripsentinel.entity.enums.RoleType;
import com.cicconesoftware.tripsentinel.service.role.RoleService;

class RoleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(new RoleController(roleService))
                .build();
    }

    @Test
    void shouldGetAllRoles() throws Exception {
        when(roleService.getAll())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk());

        verify(roleService).getAll();
    }

    @Test
    void shouldGetRoleById() throws Exception {
        when(roleService.getById(1L))
                .thenReturn(null);

        mockMvc.perform(get("/api/roles/1"))
                .andExpect(status().isOk());

        verify(roleService).getById(1L);
    }

    @Test
    void shouldGetRoleByName() throws Exception {
        when(roleService.getByName(RoleType.CUSTOMER))
                .thenReturn(null);

        mockMvc.perform(get("/api/roles/by-name/CUSTOMER"))
                .andExpect(status().isOk());

        verify(roleService).getByName(RoleType.CUSTOMER);
    }
}