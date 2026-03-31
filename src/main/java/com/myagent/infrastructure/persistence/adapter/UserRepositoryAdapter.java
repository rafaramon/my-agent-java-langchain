package com.myagent.infrastructure.persistence.adapter;

import com.myagent.domain.model.User;
import com.myagent.domain.port.UserRepositoryPort;
import com.myagent.infrastructure.persistence.entity.UserEntity;
import com.myagent.infrastructure.persistence.repository.JpaUserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryAdapter(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = toEntity(user);
        UserEntity savedEntity = jpaUserRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaUserRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return jpaUserRepository.findByLogin(login).map(this::toDomain);
    }

    @Override
    public Optional<User> findByApiKey(String apiKey) {
        return jpaUserRepository.findByApiKey(apiKey).map(this::toDomain);
    }

    @Override
    public List<User> findAll() {
        return jpaUserRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaUserRepository.deleteById(id);
    }

    private UserEntity toEntity(User user) {
        return new UserEntity(
                user.id(),
                user.name(),
                user.description(),
                user.email(),
                user.login(),
                user.apiKey()
        );
    }

    private User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getEmail(),
                entity.getLogin(),
                entity.getApiKey()
        );
    }
}
