package com.sweetshop.backend.auth.controller;

import com.sweetshop.backend.auth.dto.RegisterRequest;
import com.sweetshop.backend.auth.dto.UserResponse;
import com.sweetshop.backend.auth.model.User;
import com.sweetshop.backend.auth.service.AuthService;
import jakarta.validation.Valid;
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
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
        User createdUser = authService.registerUser(request);
        UserResponse response = new UserResponse(createdUser.getId(), createdUser.getEmail());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
