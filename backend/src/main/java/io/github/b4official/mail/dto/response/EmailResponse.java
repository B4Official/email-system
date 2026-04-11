package io.github.b4official.mail.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class EmailResponse {

    private long id;
    private String subject;
    private String body;
    private String sender;
    LocalDateTime receivingTime;
}
