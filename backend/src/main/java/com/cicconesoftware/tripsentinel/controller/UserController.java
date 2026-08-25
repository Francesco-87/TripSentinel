package com.cicconesoftware.tripsentinel.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cicconesoftware.tripsentinel.dto.user.UserResponseDto;
import com.cicconesoftware.tripsentinel.dto.user.AdminCreateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.CreateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.AdminUpdateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.UserUpdateProfileRequestDto;

import com.cicconesoftware.tripsentinel.service.user.UserService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;


// REST controller that handles all HTTP requests related to user operations
@RestController
// Maps all user-related endpoints to the /api/users base path
@RequestMapping("/api/users")
// Allows requests from the frontend running on localhost:5173 to access these endpoints
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    // Constructor that injects the UserService dependency
    public UserController(UserService userService) {
        this.userService = userService;
    }
    

    // POST endpoint to create a new user; returns 201 Created status
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@RequestBody @Valid CreateUserRequestDto requestDto) {
        // Delegates to service layer to process user creation with validated input
        return userService.create(requestDto);
    }

    @PostMapping("/admin/create")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto adminCreateUser(@RequestBody @Valid AdminCreateUserRequestDto requestDto) {
        // Delegates to service layer to process admin user creation with validated input
        return userService.adminCreate(requestDto);
    }

      // GET endpoint to retrieve all users from the database
    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        // Returns a list of all users in the system
        return userService.getAll();
    }   

    // GET endpoint to retrieve a specific user by their ID
    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        // Retrieves a single user by ID from the service layer
        return userService.getById(id);
    }
    // GET endpoint to retrieve a specific user by their email address
    @GetMapping("/email")
    public UserResponseDto getUserByEmail(@RequestParam String email) {
        return userService.getByEmail(email);
    }

    @PutMapping("/admin/{id}")
    public UserResponseDto adminUpdateUser(@PathVariable Long id, @RequestBody @Valid AdminUpdateUserRequestDto updatedUser) {
        // Updates user data with validated input and returns updated user
        return userService.adminUpdate(id, updatedUser);
    }

    // PUT endpoint to fully update an existing user's information
    @PutMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdateProfileRequestDto updatedUser) {
        // Updates user data with validated input and returns updated user
        return userService.userUpdate(id, updatedUser);
    }

    // DELETE endpoint to remove a user from the database; returns 204 No Content
   /*  @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        // Deletes a user by ID; returns no content on success
        userService.deleteUser(id);
    } */

    


    
}