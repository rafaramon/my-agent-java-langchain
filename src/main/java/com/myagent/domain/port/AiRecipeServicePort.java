package com.myagent.domain.port;

import com.myagent.domain.model.Prompt;
import com.myagent.domain.model.RecipeSuggestion;

public interface AiRecipeServicePort {
    RecipeSuggestion suggestRecipe(Prompt prompt);
}
