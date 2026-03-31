package com.myagent.api.controller;

import com.myagent.api.security.ApiKeyAuthFilter;
import com.myagent.api.security.WebSecurityConfig;
import com.myagent.application.usecase.GetAllUsersUseCase;
import com.myagent.application.usecase.GetUserUseCase;
import com.myagent.domain.model.User;
import com.myagent.domain.port.PasswordEncoderPort;
import com.myagent.domain.port.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GetUserController.class)
@Import({WebSecurityConfig.class, ApiKeyAuthFilter.class})
class GetUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetUserUseCase getUserUseCase;

    @MockitoBean
    private GetAllUsersUseCase getAllUsersUseCase;

    @MockitoBean
    private UserRepositoryPort userRepository;

    @MockitoBean
    private PasswordEncoderPort passwordEncoder;

    @Test
    void shouldReturnUserByIdWhenExists() throws Exception {
        UUID id = UUID.randomUUID();
        User user = new User(id, "John", "Dev", "john@example.com", "johnl", "hashed");
        String validKey = "valid-key";

        when(passwordEncoder.encode(validKey)).thenReturn("hashed");
        when(userRepository.findByApiKey("hashed")).thenReturn(Optional.of(user));
        when(getUserUseCase.execute(id)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/v1/users/{id}", id)
                        .header("X-API-Key", validKey))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        UUID id = UUID.randomUUID();
        String validKey = "valid-key";
        User admin = new User(UUID.randomUUID(), "Admin", "Admin", "a@a.com", "admin", "hashed-admin");

        when(passwordEncoder.encode(validKey)).thenReturn("hashed-admin");
        when(userRepository.findByApiKey("hashed-admin")).thenReturn(Optional.of(admin));
        when(getUserUseCase.execute(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/users/{id}", id)
                        .header("X-API-Key", validKey))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        User user = new User(UUID.randomUUID(), "John", "Dev", "john@example.com", "johnl", "hashed");
        String validKey = "valid-key";

        when(passwordEncoder.encode(validKey)).thenReturn("hashed");
        when(userRepository.findByApiKey("hashed")).thenReturn(Optional.of(user));
        when(getAllUsersUseCase.execute()).thenReturn(Collections.singletonList(user));

        mockMvc.perform(get("/api/v1/users")
                        .header("X-API-Key", validKey))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("John"));
    }
}
