package com.myagent.domain.model;

public record Prompt(String text) {
    public Prompt {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Prompt text cannot be null or empty");
        }
    }
}
