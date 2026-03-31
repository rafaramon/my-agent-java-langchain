package com.myagent.application.usecase;

import com.myagent.domain.model.User;
import com.myagent.domain.port.PasswordEncoderPort;
import com.myagent.domain.port.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RegenerateApiKeyUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;

    public RegenerateApiKeyUseCase(UserRepositoryPort userRepository, PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String execute(UUID userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    String newToken = passwordEncoder.generateToken();
                    String hashedToken = passwordEncoder.encode(newToken);
                    
                    User updatedUser = new User(
                            user.id(),
                            user.name(),
                            user.description(),
                            user.email(),
                            user.login(),
                            hashedToken
                    );
                    
                    userRepository.save(updatedUser);
                    return newToken;
                })
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
    }
}
