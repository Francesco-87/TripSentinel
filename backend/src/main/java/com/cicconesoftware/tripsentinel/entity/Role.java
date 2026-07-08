package com.cicconesoftware.tripsentinel.entity;

import java.util.HashSet;
import java.util.Set;

import com.cicconesoftware.tripsentinel.entity.enums.RoleType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

//JPA entity for roles table in the database
@Entity
@Table(name = "roles")
public class Role {



     // Primary key for the roles entity; auto-generated 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private RoleType name;


    // Default constructor
    public Role() {
    }

    // Relationships with other entities
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    // Getters and setters for the fields

    public Long getId() {
        return id;
    }

    public RoleType getName() {
        return name;
    }

    public void setName(RoleType name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }    
    
}
