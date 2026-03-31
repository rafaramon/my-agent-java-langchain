package com.myagent.api.controller;

import com.myagent.api.dto.UserResponse;
import com.myagent.application.usecase.GetAllUsersUseCase;
import com.myagent.application.usecase.GetUserUseCase;
import com.myagent.domain.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class GetUserController {

    private final GetUserUseCase getUserUseCase;
    private final GetAllUsersUseCase getAllUsersUseCase;

    public GetUserController(GetUserUseCase getUserUseCase, GetAllUsersUseCase getAllUsersUseCase) {
        this.getUserUseCase = getUserUseCase;
        this.getAllUsersUseCase = getAllUsersUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable UUID id) {
        return getUserUseCase.execute(id)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<UserResponse> getAll() {
        return getAllUsersUseCase.execute().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.id(),
                user.name(),
                user.description(),
                user.email(),
                user.login(),
                user.apiKey());
    }
}
