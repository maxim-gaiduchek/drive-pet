package cz.cvut.fit.ejk.gaidumax.drive.dto.security;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LoginDto {

    @NotBlank(message = "Login must not be empty")
    private String login;
    @NotBlank(message = "Password must not be empty")
    private String password;
}
