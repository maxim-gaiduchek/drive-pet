package cz.cvut.fit.ejk.gaidumax.drive.dto.security;

import jakarta.validation.constraints.Email;
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
public class RegistrationDto {

    @NotBlank(message = "First name must not be empty")
    private String firstName;
    @NotBlank(message = "Last name must not be empty")
    private String lastName;
    @NotBlank(message = "Email must not be empty")
    @Email(message = "Email is invalid")
    private String email;
    @NotBlank(message = "Password must not be empty")
    private String password;
}
