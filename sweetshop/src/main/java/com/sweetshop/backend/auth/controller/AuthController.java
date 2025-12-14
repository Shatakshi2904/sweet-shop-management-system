package com.sweetshop.backend.auth.controller;

import com.sweetshop.backend.auth.model.User;
import com.sweetshop.backend.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/api/auth/register")
    public ResponseEntity<User> registerUser(@RequestBody User userRequest) {
        User createdUser = authService.registerUser(userRequest);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}
