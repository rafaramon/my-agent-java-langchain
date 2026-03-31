package com.myagent.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myagent.api.dto.UserRequest;
import com.myagent.api.security.ApiKeyAuthFilter;
import com.myagent.api.security.WebSecurityConfig;
import com.myagent.application.usecase.CreateUserUseCase;
import com.myagent.domain.model.User;
import com.myagent.domain.port.PasswordEncoderPort;
import com.myagent.domain.port.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CreateUserController.class)
@Import({WebSecurityConfig.class, ApiKeyAuthFilter.class})
class CreateUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateUserUseCase createUserUseCase;

    @MockitoBean
    private UserRepositoryPort userRepository;

    @MockitoBean
    private PasswordEncoderPort passwordEncoder;

    @Test
    void shouldCreateUserWhenApiKeyIsValid() throws Exception {
        String validRawKey = "valid-key";
        String hashedKey = "hashed-key";
        UserRequest request = new UserRequest("John", "Dev", "john@example.com", "johnl", "secret");
        User createdUser = new User(UUID.randomUUID(), "John", "Dev", "john@example.com", "johnl", hashedKey);

        when(passwordEncoder.encode(validRawKey)).thenReturn(hashedKey);
        when(userRepository.findByApiKey(hashedKey)).thenReturn(Optional.of(createdUser));
        when(createUserUseCase.execute(any(User.class))).thenReturn(createdUser);

        mockMvc.perform(post("/api/v1/users")
                        .header("X-API-Key", validRawKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.login").value("johnl"));
    }

    @Test
    void shouldReturnUnauthorizedWhenApiKeyIsMissing() throws Exception {
        UserRequest request = new UserRequest("John", "Dev", "john@example.com", "johnl", "secret");

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWhenApiKeyIsInvalid() throws Exception {
        String invalidKey = "invalid-key";
        String hashedInvalidKey = "hashed-invalid";
        UserRequest request = new UserRequest("John", "Dev", "john@example.com", "johnl", "secret");

        when(passwordEncoder.encode(invalidKey)).thenReturn(hashedInvalidKey);
        when(userRepository.findByApiKey(hashedInvalidKey)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/users")
                        .header("X-API-Key", invalidKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}
