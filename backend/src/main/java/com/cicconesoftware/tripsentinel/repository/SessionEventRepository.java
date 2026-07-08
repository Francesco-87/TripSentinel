package com.cicconesoftware.tripsentinel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cicconesoftware.tripsentinel.entity.SessionEvent;

public interface SessionEventRepository extends JpaRepository<SessionEvent, Long> {
    
}
