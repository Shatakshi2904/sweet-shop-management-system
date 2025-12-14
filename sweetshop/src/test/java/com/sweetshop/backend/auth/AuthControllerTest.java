package com.sweetshop.backend.auth;

import com.sweetshop.backend.auth.controller.AuthController;
import com.sweetshop.backend.auth.model.User;
import com.sweetshop.backend.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // disables security filters
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    void shouldRegisterUserUsingJsonAndCallService() throws Exception {
        // given
        String requestBody = """
                {
                  "email": "test@example.com",
                  "password": "secret123"
                }
                """;

        Mockito.when(authService.registerUser(Mockito.any(User.class)))
                .thenReturn(new User("1", "test@example.com", "secret123"));

        // when + then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        // verify controller delegates to service
        verify(authService, times(1)).registerUser(Mockito.any(User.class));
    }
}
