package io.b4official.mail.repository;

import io.b4official.mail.domain.User;

import java.util.List;

public class DatabaseUserRepository implements UserRepository{
    @Override
    public void addUser() {

    }

    @Override
    public List<User> getUsers() {
        return List.of();
    }
}
