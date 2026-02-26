package io.github.b4official.mail.repository;

import io.github.b4official.mail.domain.User;

import java.util.List;

public class DatabaseEmailRepository implements UserRepository{
    @Override
    public void addUser() {

    }

    @Override
    public List<User> getUsers() {
        return List.of();
    }
}
