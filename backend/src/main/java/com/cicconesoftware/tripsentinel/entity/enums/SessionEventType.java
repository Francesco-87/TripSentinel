package com.cicconesoftware.tripsentinel.entity.enums;

/** Defines the supported session event type values. */
public enum SessionEventType {
    CREATED,
    RESPONDER_ASSIGNED,
    CHECK_IN_REMINDER_SENT,
    CHECK_IN_COMPLETED,
    MISSED,
    ESCALATED,
    CANCELLED
}
