package com.cicconesoftware.tripsentinel.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

//JPA entity for the CheckInMethod table in the database
@Entity
@Table(name = "check_in_methods")
public class CheckInMethod {

   //primary key for the CheckInMethod entity; auto-generated
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    // Default constructor
    public CheckInMethod() {}

    //Relationships with other entities

    @ManyToMany(mappedBy = "checkInMethods")
    private Set<CheckInSession> sessions = new HashSet<>();


    // Getters and setters for the fields

    public Long getId() {
        return id;
    }
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<CheckInSession> getSessions() {
        return sessions;
    }

    public void setSessions(Set<CheckInSession> sessions) {
        this.sessions = sessions;
    }
    
}
