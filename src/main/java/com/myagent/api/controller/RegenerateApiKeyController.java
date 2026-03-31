package com.myagent.api.controller;

import com.myagent.api.dto.ApiKeyResponse;
import com.myagent.application.usecase.RegenerateApiKeyUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class RegenerateApiKeyController {

    private final RegenerateApiKeyUseCase regenerateApiKeyUseCase;

    public RegenerateApiKeyController(RegenerateApiKeyUseCase regenerateApiKeyUseCase) {
        this.regenerateApiKeyUseCase = regenerateApiKeyUseCase;
    }

    @PostMapping("/{id}/api-key")
    public ResponseEntity<ApiKeyResponse> regenerate(@PathVariable UUID id) {
        try {
            String newKey = regenerateApiKeyUseCase.execute(id);
            return ResponseEntity.ok(new ApiKeyResponse(newKey));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
