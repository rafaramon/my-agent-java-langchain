package com.myagent.application.usecase;

import com.myagent.domain.model.User;
import com.myagent.domain.port.UserRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class CreateUserUseCase {
    private final UserRepositoryPort userRepository;
    private final com.myagent.domain.port.PasswordEncoderPort passwordEncoder;

    public CreateUserUseCase(UserRepositoryPort userRepository, com.myagent.domain.port.PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User execute(User user) {
        User userWithHashedKey = new User(
                user.id(),
                user.name(),
                user.description(),
                user.email(),
                user.login(),
                passwordEncoder.encode(user.apiKey())
        );
        return userRepository.save(userWithHashedKey);
    }
}
