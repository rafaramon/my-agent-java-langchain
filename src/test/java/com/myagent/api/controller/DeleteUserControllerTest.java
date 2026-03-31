package com.myagent.api.controller;

import com.myagent.api.security.ApiKeyAuthFilter;
import com.myagent.api.security.WebSecurityConfig;
import com.myagent.application.usecase.DeleteUserUseCase;
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

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeleteUserController.class)
@Import({WebSecurityConfig.class, ApiKeyAuthFilter.class})
class DeleteUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DeleteUserUseCase deleteUserUseCase;

    @MockitoBean
    private UserRepositoryPort userRepository;

    @MockitoBean
    private PasswordEncoderPort passwordEncoder;

    @Test
    void shouldDeleteUserWhenApiKeyIsValid() throws Exception {
        UUID id = UUID.randomUUID();
        String validKey = "valid-key";
        String hashedKey = "hashed-key";
        User admin = new User(UUID.randomUUID(), "Admin", "Admin", "a@a.com", "admin", hashedKey);

        when(passwordEncoder.encode(validKey)).thenReturn(hashedKey);
        when(userRepository.findByApiKey(hashedKey)).thenReturn(Optional.of(admin));
        doNothing().when(deleteUserUseCase).execute(id);

        mockMvc.perform(delete("/api/v1/users/{id}", id)
                        .header("X-API-Key", validKey))
                .andExpect(status().isNoContent());
    }
}
