package com.cicconesoftware.tripsentinel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cicconesoftware.tripsentinel.entity.CheckInSession;

/** Provides persistence operations for check in session records. */
public interface CheckInSessionRepository extends JpaRepository<CheckInSession, Long> {
/** Finds sessions by customer without guaranteeing result order. */
List<CheckInSession> findByCustomerId(Long customerId);

/** Finds sessions by assigned responder without guaranteeing result order. */
List<CheckInSession> findByResponderId(Long responderId);

}
