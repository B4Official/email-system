package io.github.b4official.mail.repository;

import io.github.b4official.mail.domain.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryUserRepository {

    private final List<User> users = new ArrayList<>();

    public InMemoryUserRepository() {
        users.add(new User(1L, "emibz", "emi", "buzincu", "emi@test", "temporary"));
    }

    public Optional<User> findByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }
}