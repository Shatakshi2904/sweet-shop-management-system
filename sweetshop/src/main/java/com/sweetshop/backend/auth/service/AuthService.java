package com.sweetshop.backend.auth.service;

import com.sweetshop.backend.auth.dto.RegisterRequest;
import com.sweetshop.backend.auth.model.User;
import com.sweetshop.backend.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        // later you will hash the password here
        User user = new User(null, request.getEmail(), request.getPassword());
        return userRepository.save(user);
    }
}
