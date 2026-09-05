package com.cicconesoftware.tripsentinel.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.cicconesoftware.tripsentinel.entity.enums.SessionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "check_in_sessions")
/** Represents a check in session persisted by JPA. */
public class CheckInSession {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_at", nullable = false)
    private Instant startAt;

    @Column(name = "expected_return_at", nullable = false)
    private Instant expectedReturnAt;

    @Column(name = "latest_check_in_at", nullable = false)
    private Instant latestCheckInAt;

    @Column(name = "time_zone", nullable = false, length = 63)
    private String timeZone;

    @Column(name = "location_description", nullable = false, columnDefinition = "TEXT")
    private String locationDescription;

    @Column(name = "important_notes", columnDefinition = "TEXT")
    private String importantNotes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private SessionStatus status;

    @Column(name  = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public CheckInSession() {
    }


    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne
    @JoinColumn(name = "responder_id", nullable = false)
    private User responder;

    @ManyToMany
    @JoinTable(
        name = "session_check_in_methods",
        joinColumns = @JoinColumn(name = "session_id"),
        inverseJoinColumns = @JoinColumn(name = "method_id")
    )
    private Set<CheckInMethod> checkInMethods = new HashSet<>();

    @OneToMany(mappedBy = "session")
    private Set<SessionEvent> events = new HashSet<>();


    public Long getId() {
        return id;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public User getResponder() {
        return responder;
    }

    public void setResponder(User responder) {
        this.responder = responder;
    }

    public Set<CheckInMethod> getCheckInMethods() {
    return checkInMethods;
    }

    public void setCheckInMethods(Set<CheckInMethod> checkInMethods) {
        this.checkInMethods = checkInMethods;
    }

    public Set<SessionEvent> getEvents() {
        return events;
    }

    public void setEvents(Set<SessionEvent> events) {
        this.events = events;
    }

    public Instant getStartAt() {
        return startAt;
    }

    public void setStartAt(Instant startAt) {
        this.startAt = startAt;
    }


    public Instant getExpectedReturnAt() {
        return expectedReturnAt;
    }

    public void setExpectedReturnAt(Instant expectedReturnAt) {
        this.expectedReturnAt = expectedReturnAt;
    }

    /** Returns the customer-defined deadline after which the session may escalate. */
    public Instant getLatestCheckInAt() {
        return latestCheckInAt;
    }

    public void setLatestCheckInAt(Instant latestCheckInAt) {
        this.latestCheckInAt = latestCheckInAt;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }



    
}
