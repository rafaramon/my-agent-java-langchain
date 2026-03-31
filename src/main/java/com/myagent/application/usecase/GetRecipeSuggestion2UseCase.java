package com.myagent.application.usecase;

import com.myagent.domain.model.Prompt;
import com.myagent.domain.model.RecipeSuggestion;
import com.myagent.domain.port.AiRecipeService2Port;

public class GetRecipeSuggestion2UseCase {

    private final AiRecipeService2Port aiRecipeService2Port;

    public GetRecipeSuggestion2UseCase(AiRecipeService2Port aiRecipeService2Port) {
        this.aiRecipeService2Port = aiRecipeService2Port;
    }

    public RecipeSuggestion execute(String promptText) {
        if (promptText == null || promptText.trim().isEmpty()) {
            throw new IllegalArgumentException("Prompt text cannot be empty");
        }
        Prompt prompt = new Prompt(promptText);
        return aiRecipeService2Port.suggestRecipe(prompt);
    }
}
