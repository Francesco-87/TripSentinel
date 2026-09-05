package com.cicconesoftware.tripsentinel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cicconesoftware.tripsentinel.entity.ResponderAvailability;

/** Provides persistence operations for responder availability records. */
public interface ResponderAvailabilityRepository extends JpaRepository<ResponderAvailability, Long> {

    /** Finds a responder's availability slots without guaranteeing result order. */
    List<ResponderAvailability> findByResponderId(Long responderId);
}
