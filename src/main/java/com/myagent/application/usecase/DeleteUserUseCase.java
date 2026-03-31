package com.myagent.application.usecase;

import com.myagent.domain.port.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteUserUseCase {
    private final UserRepositoryPort userRepository;

    public DeleteUserUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(UUID id) {
        userRepository.deleteById(id);
    }
}
