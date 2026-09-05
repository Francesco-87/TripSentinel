package com.cicconesoftware.tripsentinel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cicconesoftware.tripsentinel.entity.Role;
import com.cicconesoftware.tripsentinel.entity.enums.RoleType;

/** Provides persistence operations for role records. */
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleType name);
    
}
