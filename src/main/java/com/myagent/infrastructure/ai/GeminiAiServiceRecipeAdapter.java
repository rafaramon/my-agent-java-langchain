package com.myagent.infrastructure.ai;

import com.myagent.domain.model.Prompt;
import com.myagent.domain.model.RecipeSuggestion;
import com.myagent.domain.port.AiRecipeService2Port;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeminiAiServiceRecipeAdapter implements AiRecipeService2Port {

    private final RecipeAiAssistant recipeAiAssistant;

    public GeminiAiServiceRecipeAdapter(RecipeAiAssistant recipeAiAssistant) {
        this.recipeAiAssistant = recipeAiAssistant;
    }

    @Override
    public RecipeSuggestion suggestRecipe(Prompt prompt) {
        String input = prompt.text();
        
        try {
            String responseText = recipeAiAssistant.suggestRecipe(input);
            return new RecipeSuggestion(responseText);
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("inappropriate content detected")) {
                throw new IllegalArgumentException("Prompt rejected by guardrail: inappropriate content detected.");
            }
            throw e;
        }
    }
}
