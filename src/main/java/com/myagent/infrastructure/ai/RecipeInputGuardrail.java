package com.myagent.infrastructure.ai;

import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailResult;
import dev.langchain4j.data.message.UserMessage;
import java.util.List;

public class RecipeInputGuardrail implements InputGuardrail {

    private static final List<String> BLOCKED_KEYWORDS = List.of("hack", "bomb", "illegal", "drugs");

    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String input = userMessage.singleText();

        for (String keyword : BLOCKED_KEYWORDS) {
            if (input.toLowerCase().contains(keyword)) {
                return fatal("Prompt rejected by guardrail: inappropriate content detected.");
            }
        }

        return success();
    }
}
