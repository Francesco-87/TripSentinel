package com.cicconesoftware.tripsentinel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cicconesoftware.tripsentinel.entity.CheckInMethod;
import com.cicconesoftware.tripsentinel.entity.enums.CheckInMethodType;

public interface CheckInMethodRepository extends JpaRepository<CheckInMethod, Long> {

    Optional<CheckInMethod> findByName(CheckInMethodType name);
    
}
