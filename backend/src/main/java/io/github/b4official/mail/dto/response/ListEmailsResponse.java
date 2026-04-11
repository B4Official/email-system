package io.github.b4official.mail.dto.response;

import io.github.b4official.mail.domain.Email;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class ListEmailsResponse {

    private List<EmailResponse> receivedEmails;
}
