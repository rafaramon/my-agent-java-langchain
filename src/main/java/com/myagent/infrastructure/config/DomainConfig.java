package com.myagent.infrastructure.config;

import com.myagent.application.usecase.GetRecipeSuggestionUseCase;
import com.myagent.domain.port.AiRecipeServicePort;
import com.myagent.domain.service.RecipeDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    public RecipeDomainService recipeDomainService(AiRecipeServicePort aiRecipeServicePort) {
        return new RecipeDomainService(aiRecipeServicePort);
    }

    @Bean
    public GetRecipeSuggestionUseCase getRecipeSuggestionUseCase(RecipeDomainService recipeDomainService) {
        return new GetRecipeSuggestionUseCase(recipeDomainService);
    }
}
