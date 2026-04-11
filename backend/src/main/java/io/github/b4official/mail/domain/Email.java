package io.github.b4official.mail.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Email {

    private Long id;
    private User sender;
    private User receiver;
    private String subject;
    private String body;

    @Builder.Default
    LocalDateTime sentTime = LocalDateTime.now();

}
