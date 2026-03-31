package com.myagent.api.dto;

import java.util.UUID;

public record UserResponse(
    UUID id,
    String name,
    String description,
    String email,
    String login,
    String apiKey
) {
}
