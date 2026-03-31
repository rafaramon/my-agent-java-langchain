package com.myagent.domain.port;

import com.myagent.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByLogin(String login);
    Optional<User> findByApiKey(String apiKey);
    List<User> findAll();
    void deleteById(UUID id);
}
