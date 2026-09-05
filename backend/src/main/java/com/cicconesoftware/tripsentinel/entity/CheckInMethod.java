package com.cicconesoftware.tripsentinel.entity;

import java.util.HashSet;
import java.util.Set;

import com.cicconesoftware.tripsentinel.entity.enums.CheckInMethodType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "check_in_methods")
/** Represents a check in method persisted by JPA. */
public class CheckInMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private CheckInMethodType name;

    public CheckInMethod() {}


    @ManyToMany(mappedBy = "checkInMethods")
    private Set<CheckInSession> sessions = new HashSet<>();



    public Long getId() {
        return id;
    }
    

    public CheckInMethodType getName() {
        return name;
    }

    public void setName(CheckInMethodType name) {
        this.name = name;
    }

    public Set<CheckInSession> getSessions() {
        return sessions;
    }

    public void setSessions(Set<CheckInSession> sessions) {
        this.sessions = sessions;
    }
    
}
