package com.sweetshop.backend.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sweetshop.backend.auth.controller.AuthController;
import com.sweetshop.backend.auth.dto.LoginRequest;
import com.sweetshop.backend.auth.dto.RegisterRequest;
import com.sweetshop.backend.auth.model.User;
import com.sweetshop.backend.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.sweetshop.backend.auth.dto.LoginResponse;

@WebMvcTest(controllers = AuthController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                com.sweetshop.backend.config.SecurityConfig.class,
                com.sweetshop.backend.security.JwtAuthenticationFilter.class
        }))
@AutoConfigureMockMvc(addFilters = false) // disable security filters in this slice test
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    void shouldRegisterUserUsingJsonAndCallService() throws Exception {
        // given
        RegisterRequest request = new RegisterRequest("test@example.com", "secret123");
        User user = new User("1", "test@example.com", "encoded-secret", "USER");

        when(authService.registerUser(any(RegisterRequest.class))).thenReturn(user);

        // when + then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(authService, times(1)).registerUser(any(RegisterRequest.class));
    }

    @Test
    void shouldReturnBadRequestForInvalidRegistration() throws Exception {
        // given: invalid payload (empty email and short password)
        RegisterRequest invalid = new RegisterRequest("", "123");

        // when + then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldLoginWithValidCredentialsAndReturnTokenJson() throws Exception {
        LoginRequest request = new LoginRequest("test@example.com", "secret123");
        LoginResponse response = new LoginResponse("test@example.com", "dummy-token");

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.token").value("dummy-token"));

        verify(authService, times(1)).login(any(LoginRequest.class));
    }

    @Test
    void shouldRejectLoginWithInvalidCredentials() throws Exception {
        LoginRequest request = new LoginRequest("test@example.com", "wrong");

        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new IllegalArgumentException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

}