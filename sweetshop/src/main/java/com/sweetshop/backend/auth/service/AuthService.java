package com.sweetshop.backend.auth.service;

import com.sweetshop.backend.auth.dto.LoginRequest;
import com.sweetshop.backend.auth.dto.LoginResponse;
import com.sweetshop.backend.auth.dto.RegisterRequest;
import com.sweetshop.backend.auth.model.User;
import com.sweetshop.backend.auth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(null, request.getEmail(), encodedPassword);
        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user = findUserByEmailOrThrow(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        // For now, generate a dummy token; later you can replace with real JWT.
        String token = generateTokenForUser(user);

        return new LoginResponse(user.getEmail(), token);
    }

    private User findUserByEmailOrThrow(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        return userOpt.orElseThrow(
                () -> new IllegalArgumentException("Invalid credentials"));
    }

    private String generateTokenForUser(User user) {
        // placeholder implementation; swap with real JWT generation later
        return "token-" + UUID.randomUUID();
    }
}
