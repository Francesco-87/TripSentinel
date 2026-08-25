package com.cicconesoftware.tripsentinel.service.role;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cicconesoftware.tripsentinel.dto.role.RoleResponseDto;
import com.cicconesoftware.tripsentinel.entity.Role;
import com.cicconesoftware.tripsentinel.entity.enums.RoleType;
import com.cicconesoftware.tripsentinel.mapper.role.RoleMapper;
import com.cicconesoftware.tripsentinel.repository.RoleRepository;

class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    private RoleService roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        RoleMapper roleMapper = new RoleMapper();

        roleService = new RoleServiceImpl(
                roleMapper,
                roleRepository
        );
    }

    @Test
    void shouldGetRoleById() {
        // Arrange
        Role role = new Role();
        role.setName(RoleType.ADMIN);

        when(roleRepository.findById(1L))
                .thenReturn(Optional.of(role));

        // Act
        RoleResponseDto result = roleService.getById(1L);

        // Assert
        assertEquals(RoleType.ADMIN, result.getName());

        verify(roleRepository).findById(1L);
    }

    @Test
    void shouldGetRoleByName() {
        // Arrange
        Role role = new Role();
        role.setName(RoleType.RESPONDER);

        when(roleRepository.findByName(RoleType.RESPONDER))
                .thenReturn(Optional.of(role));

        // Act
        RoleResponseDto result =
                roleService.getByName(RoleType.RESPONDER);

        // Assert
        assertEquals(RoleType.RESPONDER, result.getName());

        verify(roleRepository).findByName(RoleType.RESPONDER);
    }

    @Test
    void shouldGetAllRoles() {
        // Arrange
        Role admin = new Role();
        admin.setName(RoleType.ADMIN);

        Role responder = new Role();
        responder.setName(RoleType.RESPONDER);

        when(roleRepository.findAll())
                .thenReturn(List.of(admin, responder));

        // Act
        List<RoleResponseDto> result = roleService.getAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals(RoleType.ADMIN, result.get(0).getName());
        assertEquals(RoleType.RESPONDER, result.get(1).getName());

        verify(roleRepository).findAll();
    }
}