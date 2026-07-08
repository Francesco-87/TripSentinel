package com.cicconesoftware.tripsentinel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cicconesoftware.tripsentinel.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    
}
