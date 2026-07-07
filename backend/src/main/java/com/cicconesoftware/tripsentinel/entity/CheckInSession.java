package com.cicconesoftware.tripsentinel.entity;

import java.time.LocalDateTime;

import com.cicconesoftware.tripsentinel.entity.enums.SessionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "check_in_sessions")
public class CheckInSession {
    
    //primary key for the CheckInSession entity; auto-generated
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="customer_id", nullable = false)
    private Long customerId;

    @Column(name = "responder_id", nullable = false)
    private Long responderId;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;    

    @Column(name = "latest_check_in_at", nullable = false)
    private LocalDateTime latestCheckInAt;  

    @Column(name = "location_description", nullable = false, columnDefinition = "TEXT")
    private String locationDescription;

    @Column(name = "important_notes", columnDefinition = "TEXT")
    private String importantNotes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private SessionStatus status;

    @Column(name  = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public CheckInSession() {
    }


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getResponderId() {
        return responderId;
    }

    public void setResponderId(Long responderId) {
        this.responderId = responderId;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public void setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }

    public LocalDateTime getLatestCheckInAt() {
        return latestCheckInAt;
    }

    public void setLatestCheckInAt(LocalDateTime latestCheckInAt) {
        this.latestCheckInAt = latestCheckInAt;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public String getImportantNotes() {
        return importantNotes;
    }

    public void setImportantNotes(String importantNotes) {
        this.importantNotes = importantNotes;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }



    
}
