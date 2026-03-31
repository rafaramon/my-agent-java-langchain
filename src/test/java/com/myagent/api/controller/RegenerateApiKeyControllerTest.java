package com.myagent.api.controller;

import com.myagent.api.security.ApiKeyAuthFilter;
import com.myagent.api.security.WebSecurityConfig;
import com.myagent.application.usecase.RegenerateApiKeyUseCase;
import com.myagent.domain.model.User;
import com.myagent.domain.port.PasswordEncoderPort;
import com.myagent.domain.port.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegenerateApiKeyController.class)
@Import({WebSecurityConfig.class, ApiKeyAuthFilter.class})
class RegenerateApiKeyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegenerateApiKeyUseCase regenerateApiKeyUseCase;

    @MockitoBean
    private UserRepositoryPort userRepository;

    @MockitoBean
    private PasswordEncoderPort passwordEncoder;

    @Test
    void shouldRegenerateApiKeyWhenUserExists() throws Exception {
        UUID id = UUID.randomUUID();
        String validKey = "valid-key";
        String hashedKey = "hashed-key";
        String newKey = "new-random-key";
        User admin = new User(UUID.randomUUID(), "Admin", "Admin", "a@a.com", "admin", hashedKey);

        when(passwordEncoder.encode(validKey)).thenReturn(hashedKey);
        when(userRepository.findByApiKey(hashedKey)).thenReturn(Optional.of(admin));
        when(regenerateApiKeyUseCase.execute(id)).thenReturn(newKey);

        mockMvc.perform(post("/api/v1/users/{id}/api-key", id)
                        .header("X-API-Key", validKey))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.newApiKey").value(newKey));
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        UUID id = UUID.randomUUID();
        String validKey = "valid-key";
        String hashedKey = "hashed-key";
        User admin = new User(UUID.randomUUID(), "Admin", "Admin", "a@a.com", "admin", hashedKey);

        when(passwordEncoder.encode(validKey)).thenReturn(hashedKey);
        when(userRepository.findByApiKey(hashedKey)).thenReturn(Optional.of(admin));
        when(regenerateApiKeyUseCase.execute(id)).thenThrow(new IllegalArgumentException("User not found"));

        mockMvc.perform(post("/api/v1/users/{id}/api-key", id)
                        .header("X-API-Key", validKey))
                .andExpect(status().isNotFound());
    }
}
