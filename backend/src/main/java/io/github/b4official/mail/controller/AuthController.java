package io.github.b4official.mail.controller;

import io.github.b4official.mail.dto.*;
import io.github.b4official.mail.dto.request.LoginRequest;
import io.github.b4official.mail.dto.response.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){

        System.out.println(loginRequest.getUsername() + " connected");

        LoginResponse test = LoginResponse.builder()
                .token("test token")
                .username("Alex")
                .id(22L)
                .email("test@gmail.com")
                .build();

        return ResponseEntity.ok(test);
    }





}
