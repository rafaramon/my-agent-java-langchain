package com.myagent.domain.model;

public record RecipeSuggestion(String content) {
    public RecipeSuggestion {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Recipe suggestion content cannot be null or empty");
        }
    }
}
