package com.cicconesoftware.tripsentinel.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cicconesoftware.tripsentinel.dto.user.UserResponseDto;
import com.cicconesoftware.tripsentinel.dto.user.AdminCreateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.AdminPatchUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.CreateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.AdminUpdateUserRequestDto;
import com.cicconesoftware.tripsentinel.dto.user.UserUpdateProfileRequestDto;

import com.cicconesoftware.tripsentinel.service.user.UserService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
/** Exposes HTTP endpoints for user operations. */
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@RequestBody @Valid CreateUserRequestDto requestDto) {
        return userService.create(requestDto);
    }

    @PostMapping("/admin/create")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto adminCreateUser(@RequestBody @Valid AdminCreateUserRequestDto requestDto) {
        return userService.adminCreate(requestDto);
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAll();
    }   

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userService.getById(id);
    }
    @GetMapping("/email")
    public UserResponseDto getUserByEmail(@RequestParam String email) {
        return userService.getByEmail(email);
    }

    @PatchMapping("/admin/{id}")
    public UserResponseDto adminPatchUser(@PathVariable Long id, @RequestBody @Valid AdminPatchUserRequestDto updatedUser) {
        return userService.adminPatch(id, updatedUser);
    }

    @PutMapping("/admin/{id}")
    public UserResponseDto adminUpdateUser(@PathVariable Long id, @RequestBody @Valid AdminUpdateUserRequestDto updatedUser) {
        return userService.adminUpdate(id, updatedUser);
    }

    @PutMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdateProfileRequestDto updatedUser) {
        return userService.userUpdate(id, updatedUser);
    }

}
