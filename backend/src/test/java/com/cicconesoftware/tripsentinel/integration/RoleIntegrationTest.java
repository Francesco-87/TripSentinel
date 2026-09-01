package com.cicconesoftware.tripsentinel.integration;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.cicconesoftware.tripsentinel.entity.Role;
import com.cicconesoftware.tripsentinel.entity.enums.RoleType;
import com.cicconesoftware.tripsentinel.repository.RoleRepository;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class RoleIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleRepository roleRepository;


    @Test
    void shouldGetAllRoles() throws Exception {

        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[*].name", hasItem("ADMIN")))
                .andExpect(jsonPath("$[*].name", hasItem("CUSTOMER")))
                .andExpect(jsonPath("$[*].name", hasItem("RESPONDER")));

        assertEquals(3, roleRepository.findAll().size());
    }


    @Test
    void shouldGetRoleById() throws Exception {

        Role customerRole = roleRepository
                .findByName(RoleType.CUSTOMER)
                .orElseThrow();

        mockMvc.perform(get("/api/roles/{id}", customerRole.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerRole.getId()))
                .andExpect(jsonPath("$.name").value("CUSTOMER"));
    }


    @Test
    void shouldGetRoleByName() throws Exception {

        Role responderRole = roleRepository
                .findByName(RoleType.RESPONDER)
                .orElseThrow();

        mockMvc.perform(get("/api/roles/by-name/{name}", RoleType.RESPONDER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responderRole.getId()))
                .andExpect(jsonPath("$.name").value("RESPONDER"));
    }

    @Test
void shouldReturnNotFoundWhenRoleDoesNotExistById() throws Exception {

    mockMvc.perform(get("/api/roles/{id}", 999999L))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Not Found"))
            .andExpect(jsonPath("$.message")
                    .value("Role not found with id: 999999"))
            .andExpect(jsonPath("$.timestamp").exists());
}


@Test
void shouldReturnNotFoundWhenRoleDoesNotExistByName() throws Exception {

    mockMvc.perform(get("/api/roles/by-name/{name}", "UNKNOWN"))
            .andExpect(status().isBadRequest());
}
}