package com.sweetshop.backend.auth;

import com.sweetshop.backend.auth.dto.LoginRequest;
import com.sweetshop.backend.auth.dto.LoginResponse;
import com.sweetshop.backend.auth.model.User;
import com.sweetshop.backend.auth.repository.UserRepository;
import com.sweetshop.backend.auth.service.AuthService;
import com.sweetshop.backend.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final JwtService jwtService = mock(JwtService.class);
    private final AuthService authService = new AuthService(userRepository, passwordEncoder, jwtService);

    @Test
    void login_withValidCredentials_returnsLoginResponse() {
        // given
        LoginRequest request = new LoginRequest("a@b.com", "plainPass");
        User user = new User("1", "a@b.com", "encodedPass", "USER");

        when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("plainPass", "encodedPass")).thenReturn(true);
        when(jwtService.generateToken("a@b.com", "USER")).thenReturn("jwt-token-123");

        // when
        LoginResponse result = authService.login(request);

        // then
        assertThat(result.getEmail()).isEqualTo("a@b.com");
        assertThat(result.getToken()).isEqualTo("jwt-token-123");
        verify(jwtService, times(1)).generateToken("a@b.com", "USER");
    }

    @Test
    void login_withInvalidCredentials_throwsException() {
        // given
        LoginRequest request = new LoginRequest("a@b.com", "wrong");
        User user = new User("1", "a@b.com", "encodedPass", "USER");

        when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encodedPass")).thenReturn(false);

        // then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid credentials");
        
        verify(jwtService, never()).generateToken(anyString(), anyString());
    }

}
