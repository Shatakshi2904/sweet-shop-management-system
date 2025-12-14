package com.sweetshop.backend.auth.service;

import com.sweetshop.backend.auth.dto.LoginRequest;
import com.sweetshop.backend.auth.dto.LoginResponse;
import com.sweetshop.backend.auth.dto.RegisterRequest;
import com.sweetshop.backend.auth.model.User;
import com.sweetshop.backend.auth.repository.UserRepository;
import com.sweetshop.backend.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(null, request.getEmail(), encodedPassword, "USER");
        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user = findUserByEmailOrThrow(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String role = user.getRole() != null ? user.getRole() : "USER";
        String token = jwtService.generateToken(user.getEmail(), role);

        return new LoginResponse(user.getEmail(), token);
    }

    private User findUserByEmailOrThrow(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        return userOpt.orElseThrow(
                () -> new IllegalArgumentException("Invalid credentials"));
    }
}
