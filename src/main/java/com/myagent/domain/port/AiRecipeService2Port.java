package com.myagent.domain.port;

import com.myagent.domain.model.Prompt;
import com.myagent.domain.model.RecipeSuggestion;

public interface AiRecipeService2Port {
    RecipeSuggestion suggestRecipe(Prompt prompt);
}
