package com.myagent.api.dto;

public record UserRequest(
    String name,
    String description,
    String email,
    String login,
    String apiKey
) {
}
