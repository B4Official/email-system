package io.github.b4official.mail.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class SendEmailResponse {

    private long id;
    private LocalDateTime sentTime;


}
