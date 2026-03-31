package com.myagent.api.controller;

import com.myagent.application.usecase.GetRecipeSuggestionUseCase;
import com.myagent.api.dto.RecipeSuggestionRequest;
import com.myagent.api.dto.RecipeSuggestionResponse;
import com.myagent.domain.model.RecipeSuggestion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recipes")
public class RecipeSuggestionController {

    private final GetRecipeSuggestionUseCase getRecipeSuggestionUseCase;

    public RecipeSuggestionController(GetRecipeSuggestionUseCase getRecipeSuggestionUseCase) {
        this.getRecipeSuggestionUseCase = getRecipeSuggestionUseCase;
    }

    @PostMapping("/suggestions")
    public ResponseEntity<?> suggest(@RequestBody RecipeSuggestionRequest request) {
        try {
            RecipeSuggestion suggestion = getRecipeSuggestionUseCase.execute(request.prompt());
            return ResponseEntity.ok(new RecipeSuggestionResponse(suggestion.content()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while generating the suggestion.");
        }
    }
}
