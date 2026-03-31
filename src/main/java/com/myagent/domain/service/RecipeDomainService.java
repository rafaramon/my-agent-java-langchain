package com.myagent.domain.service;

import com.myagent.domain.model.Prompt;
import com.myagent.domain.model.RecipeSuggestion;
import com.myagent.domain.port.AiRecipeServicePort;
public class RecipeDomainService {

    private final AiRecipeServicePort aiRecipeServicePort;

    public RecipeDomainService(AiRecipeServicePort aiRecipeServicePort) {
        this.aiRecipeServicePort = aiRecipeServicePort;
    }

    public RecipeSuggestion getSuggestion(Prompt prompt) {
        validateCookingPrompt(prompt);
        return aiRecipeServicePort.suggestRecipe(prompt);
    }

    private void validateCookingPrompt(Prompt prompt) {
        String lowerCaseText = prompt.text().toLowerCase();
        // Simple heuristic blocklist to short-circuit non-cooking topics
        String[] blocklist = {"code", "programming", "python", "java", "politics", "weather", "finance", "stock", "flight"};
        
        for (String word : blocklist) {
            if (lowerCaseText.contains(word)) {
                throw new IllegalArgumentException("Prompt is unrelated to cooking. Please ask only for culinary advice.");
            }
        }
    }
}
