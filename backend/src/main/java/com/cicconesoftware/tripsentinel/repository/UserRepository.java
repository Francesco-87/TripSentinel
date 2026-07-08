package com.cicconesoftware.tripsentinel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cicconesoftware.tripsentinel.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
