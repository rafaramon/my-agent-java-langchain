package com.myagent.infrastructure.ai;

import com.myagent.domain.model.Prompt;
import com.myagent.domain.model.RecipeSuggestion;
import com.myagent.domain.port.AiRecipeServicePort;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import org.springframework.stereotype.Component;

@Component
public class GeminiRecipeAdapter implements AiRecipeServicePort {

    private final ChatModel chatModel;

    public GeminiRecipeAdapter(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public RecipeSuggestion suggestRecipe(Prompt prompt) {
        String systemPrompt = "You are a professional chef assistant. "
                + "You only provide cooking recipes and culinary advice. "
                + "If the user asks about anything unrelated to food, cooking, or recipes, "
                + "politely refuse and remind them that you can only help with culinary topics.";

        String response = chatModel.chat(
                SystemMessage.from(systemPrompt),
                UserMessage.from(prompt.text())).aiMessage().text();

        return new RecipeSuggestion(response);
    }
}
