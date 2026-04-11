package io.github.b4official.mail.controller;


import io.github.b4official.mail.dto.request.SendEmailRequest;
import io.github.b4official.mail.dto.response.EmailResponse;
import io.github.b4official.mail.dto.response.SendEmailResponse;
import io.github.b4official.mail.service.EmailService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/emails")
@RestController
@AllArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    ResponseEntity<SendEmailResponse> send(@Valid @RequestBody SendEmailRequest request){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        SendEmailResponse response = emailService.sendEmail(request, username);
        return ResponseEntity.ok(response);

    }
}
