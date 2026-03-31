package com.myagent.api.controller;

import com.myagent.api.dto.UserRequest;
import com.myagent.api.dto.UserResponse;
import com.myagent.application.usecase.CreateUserUseCase;
import com.myagent.domain.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class CreateUserController {

    private final CreateUserUseCase createUserUseCase;

    public CreateUserController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@RequestBody UserRequest request) {
        User user = new User(
                UUID.randomUUID(),
                request.name(),
                request.description(),
                request.email(),
                request.login(),
                request.apiKey());
        User created = createUserUseCase.execute(user);
        return toResponse(created);
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
