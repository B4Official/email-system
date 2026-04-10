package io.github.b4official.mail.service;

import io.github.b4official.mail.domain.User;
import io.github.b4official.mail.dto.request.LoginRequest;
import io.github.b4official.mail.dto.response.LoginResponse;
import io.github.b4official.mail.repository.InMemoryUserRepository;
import io.github.b4official.mail.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final InMemoryUserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername()).
                orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new RuntimeException("Incorrrect password");

        String jwt = jwtService.generateToken(request.getUsername());

        return LoginResponse.builder()
                .token(jwt)
                .username(user.getUsername())
                .id(user.getId())
                .email(user.getEmail())
                .build();
    }
}
