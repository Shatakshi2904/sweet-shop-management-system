package com.sweetshop.backend.auth.controller;

import com.sweetshop.backend.auth.dto.LoginRequest;
import com.sweetshop.backend.auth.dto.LoginResponse;
import com.sweetshop.backend.auth.dto.RegisterRequest;
import com.sweetshop.backend.auth.model.User;
import com.sweetshop.backend.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody RegisterRequest request) {
        User created = authService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        if (ex.getMessage() != null && ex.getMessage().contains("Invalid credentials")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
