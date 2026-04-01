package com.myagent.infrastructure.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.guardrail.InputGuardrails;
import dev.langchain4j.service.guardrail.OutputGuardrails;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface RecipeAiAssistant {

    @SystemMessage({
            "You are a professional chef assistant.",
            "You only provide cooking recipes and culinary advice.",
            "If the user asks about anything unrelated to food, cooking, or recipes, politely refuse and remind them that you can only help with culinary topics."
    })
    @InputGuardrails(RecipeInputGuardrail.class)
    // @OutputGuardrails(JsonFormatGuardrail.class)
    String suggestRecipe(@UserMessage String prompt);
}
