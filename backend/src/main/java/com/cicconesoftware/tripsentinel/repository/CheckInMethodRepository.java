package com.cicconesoftware.tripsentinel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cicconesoftware.tripsentinel.entity.CheckInMethod;

public interface CheckInMethodRepository extends JpaRepository<CheckInMethod, Long> {
    
}
