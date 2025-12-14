// imports to add:
import com.sweetshop.backend.auth.dto.LoginRequest;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Test
void shouldLoginWithValidCredentialsAndReturnUserJson() throws Exception {
    LoginRequest request = new LoginRequest("test@example.com", "secret123");
    User user = new User("1", "test@example.com", "encoded-secret");

    when(authService.login(any(LoginRequest.class))).thenReturn(user);

    mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("1"))
            .andExpect(jsonPath("$.email").value("test@example.com"));

    verify(authService).login(any(LoginRequest.class));
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
