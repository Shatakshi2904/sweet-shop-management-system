package com.sweetshop.backend.auth;

import com.sweetshop.backend.auth.dto.LoginRequest;
import com.sweetshop.backend.auth.model.User;
import com.sweetshop.backend.auth.repository.UserRepository;
import com.sweetshop.backend.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import com.sweetshop.backend.auth.dto.LoginRequest;
import com.sweetshop.backend.auth.dto.LoginResponse;


class AuthServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final AuthService authService = new AuthService(userRepository, passwordEncoder);

    @Test
    void login_withValidCredentials_returnsLoginResponse() {
        // given
        LoginRequest request = new LoginRequest("a@b.com", "plainPass");
        User user = new User("1", "a@b.com", "encodedPass");

        when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("plainPass", "encodedPass")).thenReturn(true);

        // when
        LoginResponse result = authService.login(request);

        // then
        assertThat(result.getEmail()).isEqualTo("a@b.com");
        assertThat(result.getToken()).isNotBlank();
    }

    @Test
    void login_withInvalidCredentials_throwsException() {
        // given
        LoginRequest request = new LoginRequest("a@b.com", "wrong");
        User user = new User("1", "a@b.com", "encodedPass");

        when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encodedPass")).thenReturn(false);

        // then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid credentials");
    }

}
