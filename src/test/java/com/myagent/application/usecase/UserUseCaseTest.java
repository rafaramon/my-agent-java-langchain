package com.myagent.application.usecase;

import com.myagent.domain.model.User;
import com.myagent.domain.port.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserUseCaseTest {

    private UserRepositoryPort userRepository;
    private CreateUserUseCase createUserUseCase;
    private DeleteUserUseCase deleteUserUseCase;
    private GetUserUseCase getUserUseCase;
    private GetAllUsersUseCase getAllUsersUseCase;

    // Manual implementation of UserRepositoryPort for testing (No mocks allowed)
    private static class InMemoryUserRepository implements UserRepositoryPort {
        private final java.util.Map<UUID, User> database = new java.util.HashMap<>();

        @Override
        public User save(User user) {
            database.put(user.id(), user);
            return user;
        }

        @Override
        public Optional<User> findById(UUID id) {
            return Optional.ofNullable(database.get(id));
        }

        @Override
        public Optional<User> findByLogin(String login) {
            return database.values().stream().filter(u -> u.login().equals(login)).findFirst();
        }

        @Override
        public Optional<User> findByApiKey(String apiKey) {
            return database.values().stream().filter(u -> u.apiKey().equals(apiKey)).findFirst();
        }

        @Override
        public List<User> findAll() {
            return List.copyOf(database.values());
        }

        @Override
        public void deleteById(UUID id) {
            database.remove(id);
        }
    }

    private static class PlainPasswordEncoder implements com.myagent.domain.port.PasswordEncoderPort {
        @Override
        public String encode(String rawPassword) {
            return rawPassword;
        }

        @Override
        public String generateToken() {
            return "new-token";
        }
    }

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        createUserUseCase = new CreateUserUseCase(userRepository, new PlainPasswordEncoder());
        deleteUserUseCase = new DeleteUserUseCase(userRepository);
        getUserUseCase = new GetUserUseCase(userRepository);
        getAllUsersUseCase = new GetAllUsersUseCase(userRepository);
    }

    @Test
    void shouldCreateUser() {
        User user = new User(UUID.randomUUID(), "Test", "Desc", "test@test.com", "test", "key");
        User created = createUserUseCase.execute(user);
        assertThat(userRepository.findById(created.id())).isPresent();
    }

    @Test
    void shouldDeleteUser() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "Test", "Desc", "test@test.com", "test", "key");
        userRepository.save(user);
        deleteUserUseCase.execute(id);
        assertThat(userRepository.findById(id)).isEmpty();
    }

    @Test
    void shouldGetUser() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "Test", "Desc", "test@test.com", "test", "key");
        userRepository.save(user);
        Optional<User> found = getUserUseCase.execute(id);
        assertThat(found).isPresent().contains(user);
    }

    @Test
    void shouldGetAllUsers() {
        userRepository.save(new User(UUID.randomUUID(), "U1", "D1", "e1@test.com", "l1", "k1"));
        userRepository.save(new User(UUID.randomUUID(), "U2", "D2", "e2@test.com", "l2", "k2"));
        List<User> users = getAllUsersUseCase.execute();
        assertThat(users).hasSize(2);
    }
}
