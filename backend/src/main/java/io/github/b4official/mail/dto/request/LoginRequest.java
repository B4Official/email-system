package io.github.b4official.mail.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.AllArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Username can not be empty")
    private String username;

    @NotBlank(message = "Password can not be empty")
    private String password;
}
