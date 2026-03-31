package com.myagent.application.usecase;

import com.myagent.domain.model.Prompt;
import com.myagent.domain.model.RecipeSuggestion;
import com.myagent.domain.port.AiRecipeService2Port;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetRecipeSuggestion2UseCaseTest {

    private AiRecipeService2Port aiRecipeService2Port;
    private GetRecipeSuggestion2UseCase getRecipeSuggestion2UseCase;

    @BeforeEach
    void setUp() {
        aiRecipeService2Port = mock(AiRecipeService2Port.class);
        getRecipeSuggestion2UseCase = new GetRecipeSuggestion2UseCase(aiRecipeService2Port);
    }

    @Test
    void execute_ShouldReturnRecipeSuggestion_WhenPromptIsValid() {
        // Arrange
        String promptText = "Give me a pizza recipe";
        Prompt prompt = new Prompt(promptText);
        RecipeSuggestion expectedSuggestion = new RecipeSuggestion("Here is your pizza recipe...");

        when(aiRecipeService2Port.suggestRecipe(any(Prompt.class))).thenReturn(expectedSuggestion);

        // Act
        RecipeSuggestion result = getRecipeSuggestion2UseCase.execute(promptText);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.content()).isEqualTo("Here is your pizza recipe...");
    }

    @Test
    void execute_ShouldThrowException_WhenPromptIsNull() {
        // Act
        Throwable thrown = catchThrowable(() -> getRecipeSuggestion2UseCase.execute(null));

        // Assert
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }
}
