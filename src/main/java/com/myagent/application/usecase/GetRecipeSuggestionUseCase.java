package com.myagent.application.usecase;

import com.myagent.domain.model.Prompt;
import com.myagent.domain.model.RecipeSuggestion;
import com.myagent.domain.service.RecipeDomainService;

public class GetRecipeSuggestionUseCase {
    
    private final RecipeDomainService recipeDomainService;

    public GetRecipeSuggestionUseCase(RecipeDomainService recipeDomainService) {
        this.recipeDomainService = recipeDomainService;
    }

    public RecipeSuggestion execute(String promptText) {
        Prompt prompt = new Prompt(promptText);
        return recipeDomainService.getSuggestion(prompt);
    }
}
