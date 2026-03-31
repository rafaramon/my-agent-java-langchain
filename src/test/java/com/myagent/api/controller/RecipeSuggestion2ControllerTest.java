package com.myagent.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myagent.api.dto.RecipeSuggestionRequest;
import com.myagent.api.security.ApiKeyAuthFilter;
import com.myagent.api.security.WebSecurityConfig;
import com.myagent.application.usecase.GetRecipeSuggestion2UseCase;
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

@WebMvcTest(RecipeSuggestion2Controller.class)
@Import({WebSecurityConfig.class, ApiKeyAuthFilter.class})
class RecipeSuggestion2ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GetRecipeSuggestion2UseCase getRecipeSuggestion2UseCase;

    @MockitoBean
    private UserRepositoryPort userRepository;

    @MockitoBean
    private PasswordEncoderPort passwordEncoder;

    @Test
    void shouldReturnRecipeSuggestionWhenApiKeyIsValid() throws Exception {
        String validKey = "valid-key";
        String hashedKey = "hashed-key";
        User admin = new User(UUID.randomUUID(), "Admin", "Admin", "a@a.com", "admin", hashedKey);
        
        RecipeSuggestionRequest request = new RecipeSuggestionRequest("Pizza recipe");
        RecipeSuggestion suggestion = new RecipeSuggestion("Delicious Pizza...");

        when(passwordEncoder.encode(validKey)).thenReturn(hashedKey);
        when(userRepository.findByApiKey(hashedKey)).thenReturn(Optional.of(admin));
        when(getRecipeSuggestion2UseCase.execute(anyString())).thenReturn(suggestion);

        mockMvc.perform(post("/api/v1/recipes/suggestions2")
                        .header("X-API-Key", validKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.suggestion").value("Delicious Pizza..."));
    }

    @Test
    void shouldReturnBadRequestWhenPromptIsInvalid() throws Exception {
        String validKey = "valid-key";
        String hashedKey = "hashed-key";
        User admin = new User(UUID.randomUUID(), "Admin", "Admin", "a@a.com", "admin", hashedKey);
        
        RecipeSuggestionRequest request = new RecipeSuggestionRequest("Weather in Paris");

        when(passwordEncoder.encode(validKey)).thenReturn(hashedKey);
        when(userRepository.findByApiKey(hashedKey)).thenReturn(Optional.of(admin));
        when(getRecipeSuggestion2UseCase.execute(anyString())).thenThrow(new IllegalArgumentException("Invalid content"));

        mockMvc.perform(post("/api/v1/recipes/suggestions2")
                        .header("X-API-Key", validKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
