package com.cicconesoftware.tripsentinel.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cicconesoftware.tripsentinel.entity.CheckInSession;
import com.cicconesoftware.tripsentinel.entity.enums.SessionStatus;

/** Provides persistence operations for check in session records. */
public interface CheckInSessionRepository extends JpaRepository<CheckInSession, Long> {
/** Finds sessions by customer without guaranteeing result order. */
List<CheckInSession> findByCustomerId(Long customerId);

/** Finds sessions by assigned responder without guaranteeing result order. */
List<CheckInSession> findByResponderId(Long responderId);

/** Checks whether a customer owns a session in one of the supplied non-terminal states. */
boolean existsByCustomerIdAndStatusIn(Long customerId, Set<SessionStatus> statuses);

/** Checks whether a responder is assigned to a session in one of the supplied non-terminal states. */
boolean existsByResponderIdAndStatusIn(Long responderId, Set<SessionStatus> statuses);

}
