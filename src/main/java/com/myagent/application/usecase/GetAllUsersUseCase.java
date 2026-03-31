package com.myagent.application.usecase;

import com.myagent.domain.model.User;
import com.myagent.domain.port.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllUsersUseCase {
    private final UserRepositoryPort userRepository;

    public GetAllUsersUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> execute() {
        return userRepository.findAll();
    }
}
