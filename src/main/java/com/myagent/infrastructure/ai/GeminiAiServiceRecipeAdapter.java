package com.myagent.infrastructure.ai;

import com.myagent.domain.model.Prompt;
import com.myagent.domain.model.RecipeSuggestion;
import com.myagent.domain.port.AiRecipeService2Port;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeminiAiServiceRecipeAdapter implements AiRecipeService2Port {

    private final RecipeAiAssistant recipeAiAssistant;

    // Simple programmatic test guardrail
    private static final List<String> BLOCKED_KEYWORDS = List.of("hack", "bomb", "illegal", "drugs");

    public GeminiAiServiceRecipeAdapter(RecipeAiAssistant recipeAiAssistant) {
        this.recipeAiAssistant = recipeAiAssistant;
    }

    @Override
    public RecipeSuggestion suggestRecipe(Prompt prompt) {
        String input = prompt.text();
        
        // Guardrail evaluation
        for (String keyword : BLOCKED_KEYWORDS) {
            if (input.toLowerCase().contains(keyword)) {
                throw new IllegalArgumentException("Prompt rejected by guardrail: inappropriate content detected.");
            }
        }

        String responseText = recipeAiAssistant.suggestRecipe(input);
        return new RecipeSuggestion(responseText);
    }
}
