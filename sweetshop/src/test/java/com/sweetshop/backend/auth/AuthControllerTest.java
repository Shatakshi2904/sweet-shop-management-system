package com.sweetshop.backend.auth;

import com.sweetshop.backend.auth.dto.RegisterRequest;
import com.sweetshop.backend.auth.model.User;
import com.sweetshop.backend.auth.repository.UserRepository;
import com.sweetshop.backend.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Test
    void registerUser_encodesPassword_beforeSaving() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        // Test expects encoding behavior
        when(userRepository.existsByEmail("a@b.com")).thenReturn(false);
        when(passwordEncoder.encode("plainPass")).thenReturn("encodedPass");

        // This constructor does NOT exist yet in red code -> compile error or failing test
        AuthService authService = new AuthService(userRepository, passwordEncoder);

        RegisterRequest request = new RegisterRequest("a@b.com", "plainPass");
        authService.registerUser(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        // Red expectation: encoded password must be stored
        assertThat(savedUser.getPassword()).isEqualTo("encodedPass");
        verify(passwordEncoder).encode("plainPass");
    }
}
