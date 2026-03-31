package com.myagent.infrastructure.ai;

import com.myagent.domain.model.Prompt;
import com.myagent.domain.model.RecipeSuggestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

class GeminiAiServiceRecipeAdapterTest {

    private RecipeAiAssistant recipeAiAssistant;
    private GeminiAiServiceRecipeAdapter adapter;

    @BeforeEach
    void setUp() {
        recipeAiAssistant = mock(RecipeAiAssistant.class);
        adapter = new GeminiAiServiceRecipeAdapter(recipeAiAssistant);
    }

    @Test
    void suggestRecipe_ShouldReturnSuggestion_WhenPromptIsSafe() {
        // Arrange
        Prompt prompt = new Prompt("Give me a healthy salad recipe");
        when(recipeAiAssistant.suggestRecipe("Give me a healthy salad recipe"))
                .thenReturn("Here is a fresh salad recipe");

        // Act
        RecipeSuggestion result = adapter.suggestRecipe(prompt);

        // Assert
        assertThat(result.content()).isEqualTo("Here is a fresh salad recipe");
        verify(recipeAiAssistant).suggestRecipe(anyString());
    }

    @Test
    void suggestRecipe_ShouldThrowException_WhenPromptContainsBlockedKeyword() {
        // Arrange
        Prompt prompt = new Prompt("How to make a bomb");

        // Act
        Throwable thrown = catchThrowable(() -> adapter.suggestRecipe(prompt));

        // Assert
        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("inappropriate content detected");
        
        verifyNoInteractions(recipeAiAssistant);
    }
}
