package io.github.b4official.mail.repository;

import io.github.b4official.mail.domain.User;
import java.util.List;

public interface UserRepository {

    void addUser();
    List<User> getUsers();
}
