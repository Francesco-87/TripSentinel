package com.cicconesoftware.tripsentinel.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cicconesoftware.tripsentinel.dto.role.RoleResponseDto;
import com.cicconesoftware.tripsentinel.entity.enums.RoleType;
import com.cicconesoftware.tripsentinel.service.role.RoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/roles")
// Allows requests from the frontend running on localhost:5173 to access these endpoints
@CrossOrigin(origins = "http://localhost:5173")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public List<RoleResponseDto> getAllRoles() {
        return roleService.getAll();
    }

    @GetMapping("/{id}")
    public RoleResponseDto getRoleById(@PathVariable Long id) {
        return roleService.getById(id);
    }

    @GetMapping("/by-name/{name}")
    public RoleResponseDto getRoleByName(@PathVariable RoleType name) {   
        return roleService.getByName(name);
    }
    

}
