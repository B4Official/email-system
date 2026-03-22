package io.github.b4official.mail.service;


import io.github.b4official.mail.domain.User;
import io.github.b4official.mail.dto.request.LoginRequest;
import io.github.b4official.mail.dto.response.LoginResponse;
import io.github.b4official.mail.repository.InMemoryUserRepository;
import io.github.b4official.mail.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final InMemoryUserRepository userRepository;

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername()).
                orElseThrow(() -> new RuntimeException("User not found"));

        if(!user.getPassword().equals(request.getPassword()))
            throw new RuntimeException("Incorrrect password");



        return LoginResponse.builder()
                .token("token")
                .username(user.getUsername())
                .id(user.getId())
                .email(user.getEmail())
                .build();
    }
}
