package com.myagent.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myagent.api.dto.RecipeSuggestionRequest;
import com.myagent.api.security.ApiKeyAuthFilter;
import com.myagent.api.security.WebSecurityConfig;
import com.myagent.application.usecase.GetRecipeSuggestionUseCase;
import com.myagent.domain.model.RecipeSuggestion;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipeSuggestionController.class)
@Import({WebSecurityConfig.class, ApiKeyAuthFilter.class})
class RecipeSuggestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GetRecipeSuggestionUseCase getRecipeSuggestionUseCase;

    @MockitoBean
    private UserRepositoryPort userRepository;

    @MockitoBean
    private PasswordEncoderPort passwordEncoder;

    @Test
    void shouldReturnRecipeSuggestionWhenApiKeyIsValid() throws Exception {
        String validKey = "valid-key";
        String hashedKey = "hashed-key";
        User admin = new User(UUID.randomUUID(), "Admin", "Admin", "a@a.com", "admin", hashedKey);
        
        RecipeSuggestionRequest request = new RecipeSuggestionRequest("Pasta recipe");
        RecipeSuggestion suggestion = new RecipeSuggestion("Delicious Pasta and instructions...");

        when(passwordEncoder.encode(validKey)).thenReturn(hashedKey);
        when(userRepository.findByApiKey(hashedKey)).thenReturn(Optional.of(admin));
        when(getRecipeSuggestionUseCase.execute(anyString())).thenReturn(suggestion);

        mockMvc.perform(post("/api/v1/recipes/suggestions")
                        .header("X-API-Key", validKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.suggestion").value("Delicious Pasta and instructions..."));
    }

    @Test
    void shouldReturnBadRequestWhenPromptIsInvalid() throws Exception {
        String validKey = "valid-key";
        String hashedKey = "hashed-key";
        User admin = new User(UUID.randomUUID(), "Admin", "Admin", "a@a.com", "admin", hashedKey);
        
        RecipeSuggestionRequest request = new RecipeSuggestionRequest("Weather in Paris");

        when(passwordEncoder.encode(validKey)).thenReturn(hashedKey);
        when(userRepository.findByApiKey(hashedKey)).thenReturn(Optional.of(admin));
        when(getRecipeSuggestionUseCase.execute(anyString())).thenThrow(new IllegalArgumentException("Invalid content"));

        mockMvc.perform(post("/api/v1/recipes/suggestions")
                        .header("X-API-Key", validKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
