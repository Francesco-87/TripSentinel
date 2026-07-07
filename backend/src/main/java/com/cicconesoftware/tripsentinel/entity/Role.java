package com.cicconesoftware.tripsentinel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//JPA entity for roles table in the database
@Entity
@Table(name = "roles")
public class Role {



     // Primary key for the roles entity; auto-generated 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;


    // Default constructor
    public Role() {
    }

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


    
    
}
