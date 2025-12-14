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

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final AuthService authService = new AuthService(userRepository, passwordEncoder);

    @Test
    void registerUser_encodesPassword_beforeSaving() {
        // given
        RegisterRequest request = new RegisterRequest("a@b.com", "plainPass");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPass");

        // when
        authService.registerUser(request);

        // then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getEmail()).isEqualTo("a@b.com");
        assertThat(savedUser.getPassword()).isEqualTo("encodedPass");
        verify(passwordEncoder).encode("plainPass");
    }
}
