package io.github.b4official.mail.repository;

import io.github.b4official.mail.domain.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryUserRepository {

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    private final List<User> users = new ArrayList<>();

    public InMemoryUserRepository() {
        users.add(new User(1L, "emibz", "emi", "buzincu", "emi@test.com", encoder.encode("test")));
        users.add(new User(2L, "ion99", "ion", "popescu", "ion@test.com", encoder.encode("parola")));
        users.add(new User(3L, "maria_m", "maria", "moldovan", "maria@test.com", encoder.encode("parola")));
        users.add(new User(4L, "alex_d", "alex", "dumitru", "alex@test.com", encoder.encode("parola")));
        users.add(new User(5L, "ana_b", "ana", "barbu", "ana@test.com", encoder.encode("parola")));
    }

    public Optional<User> findByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    public Optional<User> findByEmail(String email) {
        return users.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

}