package com.cicconesoftware.tripsentinel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cicconesoftware.tripsentinel.entity.SessionEvent;

/** Provides persistence operations for session event records. */
public interface SessionEventRepository extends JpaRepository<SessionEvent, Long> {

    /** Finds a session's events without guaranteeing chronological order. */
    List<SessionEvent> findBySessionId(Long sessionId);
}
