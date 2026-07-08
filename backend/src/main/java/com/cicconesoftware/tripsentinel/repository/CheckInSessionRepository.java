package com.cicconesoftware.tripsentinel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cicconesoftware.tripsentinel.entity.CheckInSession;

public interface CheckInSessionRepository extends JpaRepository<CheckInSession, Long> {
    
}
