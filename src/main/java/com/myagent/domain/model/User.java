package com.myagent.domain.model;

import java.util.UUID;

public record User(
    UUID id,
    String name,
    String description,
    String email,
    String login,
    String apiKey
) {
}
