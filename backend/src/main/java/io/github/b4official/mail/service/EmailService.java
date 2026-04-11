package io.github.b4official.mail.service;

import io.github.b4official.mail.domain.Email;
import io.github.b4official.mail.domain.User;
import io.github.b4official.mail.dto.request.SendEmailRequest;
import io.github.b4official.mail.dto.response.SendEmailResponse;
import io.github.b4official.mail.repository.EmailRepository;
import io.github.b4official.mail.repository.InMemoryEmailRepository;
import io.github.b4official.mail.repository.InMemoryUserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Builder
@Data
@AllArgsConstructor
@Service
public class EmailService {

    private final InMemoryEmailRepository emailRepository;
    private final InMemoryUserRepository userRepository;

    public SendEmailResponse sendEmail(SendEmailRequest request, String username){

        User receiver = userRepository.findByEmail(request.getReceiver())
                .orElseThrow(() -> new RuntimeException("The user doesn't exist"));

        User sender = userRepository.findByUsername(username)
                .orElseThrow();

        Email email = emailRepository.save(Email.builder()
                .sender(sender)
                .receiver(receiver)
                .subject(request.getSubject())
                .body(request.getBody())
                .build());

        return  SendEmailResponse.builder()
                .id(email.getId())
                .sentTime(email.getSentTime())
                .build();

    }

}
