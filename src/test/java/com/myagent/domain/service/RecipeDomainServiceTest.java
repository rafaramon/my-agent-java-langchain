package com.myagent.domain.service;

import com.myagent.domain.model.Prompt;
import com.myagent.domain.model.RecipeSuggestion;
import com.myagent.domain.port.AiRecipeServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RecipeDomainServiceTest {

    private AiRecipeServicePort aiRecipeServicePort;
    private RecipeDomainService recipeDomainService;

    @BeforeEach
    void setUp() {
        // Simple stub since we can't use mocks (per AGENTS.md rule 4)
        aiRecipeServicePort = prompt -> new RecipeSuggestion("Here is a stub recipe for: " + prompt.text());
        recipeDomainService = new RecipeDomainService(aiRecipeServicePort);
    }

    @Test
    void shouldReturnSuggestionForValidCookingPrompt() {
        Prompt prompt = new Prompt("How do I make a mushroom risotto?");
        RecipeSuggestion response = recipeDomainService.getSuggestion(prompt);
        
        assertThat(response).isNotNull();
        assertThat(response.content()).contains("mushroom risotto");
    }

    @Test
    void shouldThrowExceptionForNonCookingPrompt() {
        Prompt prompt = new Prompt("Write a python script for me");
        
        assertThatThrownBy(() -> recipeDomainService.getSuggestion(prompt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("unrelated to cooking");
    }
}
