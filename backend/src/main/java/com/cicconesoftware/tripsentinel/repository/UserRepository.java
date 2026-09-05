package com.cicconesoftware.tripsentinel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cicconesoftware.tripsentinel.entity.User;

/** Provides persistence operations for user records. */
public interface UserRepository extends JpaRepository<User, Long> {

    /** Finds a user using the email value exactly as supplied. */
    Optional<User> findByEmail(String email);

    /** Checks email uniqueness while excluding the user currently being updated. */
    boolean existsByEmailAndIdNot(String email, Long id);
}
