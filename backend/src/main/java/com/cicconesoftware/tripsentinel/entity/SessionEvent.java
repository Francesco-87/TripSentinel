package com.cicconesoftware.tripsentinel.entity;

import java.time.Instant;

import com.cicconesoftware.tripsentinel.entity.enums.SessionEventType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "session_events") 
/** Represents a session event persisted by JPA. */
public class SessionEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 50)
    private SessionEventType eventType;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public SessionEvent() {
    }

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private CheckInSession session;


    public Long getId() {
        return id;
    }

    public CheckInSession getSession() {
        return session;
    }

    public void setSession(CheckInSession session) {
        this.session = session;
    }

    public SessionEventType getEventType() {
        return eventType;
    }

    public void setEventType(SessionEventType eventType) {
        this.eventType = eventType;
    }
    
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

}
