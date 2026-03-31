package com.myagent.api.controller;

import com.myagent.application.usecase.GetRecipeSuggestion2UseCase;
import com.myagent.api.dto.RecipeSuggestionRequest;
import com.myagent.api.dto.RecipeSuggestionResponse;
import com.myagent.domain.model.RecipeSuggestion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/recipes")
public class RecipeSuggestion2Controller {

    private final GetRecipeSuggestion2UseCase getRecipeSuggestion2UseCase;

    public RecipeSuggestion2Controller(GetRecipeSuggestion2UseCase getRecipeSuggestion2UseCase) {
        this.getRecipeSuggestion2UseCase = getRecipeSuggestion2UseCase;
    }

    @PostMapping("/suggestions2")
    public ResponseEntity<?> suggest2(@RequestBody RecipeSuggestionRequest request) {
        try {
            RecipeSuggestion suggestion = getRecipeSuggestion2UseCase.execute(request.prompt());
            return ResponseEntity.ok(new RecipeSuggestionResponse(suggestion.content()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while generating the suggestion.");
        }
    }
}
