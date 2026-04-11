package io.github.b4official.mail.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SendEmailRequest {

    @NotBlank(message = "A receiver must be enetered")
    private String receiver;

    private String subject;

    @NotBlank(message = "The body of the mail can't be empty")
    private String body;
}
