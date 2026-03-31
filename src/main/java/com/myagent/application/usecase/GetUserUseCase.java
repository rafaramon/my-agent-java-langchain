package com.myagent.application.usecase;

import com.myagent.domain.model.User;
import com.myagent.domain.port.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class GetUserUseCase {
    private final UserRepositoryPort userRepository;

    public GetUserUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> execute(UUID id) {
        return userRepository.findById(id);
    }
}
