package cz.cvut.fit.ejk.gaidumax.drive.dto;

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
public class UserDto {

    private Long id;
    @NotBlank(message = "User first name must neither be empty nor blank")
    private String firstName;
    @NotBlank(message = "User last name must neither be empty nor blank")
    private String lastName;
    private String email;
}
