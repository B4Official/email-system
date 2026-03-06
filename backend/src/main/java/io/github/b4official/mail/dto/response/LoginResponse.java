package io.github.b4official.mail.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String token;

    @Builder.Default
    private String type = "Bearer";


    private Long id;
    private String username;
    private String email;


}
