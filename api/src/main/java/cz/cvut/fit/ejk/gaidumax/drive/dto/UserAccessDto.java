package cz.cvut.fit.ejk.gaidumax.drive.dto;

import cz.cvut.fit.ejk.gaidumax.drive.entity.UserAccessType;
import jakarta.validation.constraints.NotNull;
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
public class UserAccessDto {

    private UserDto user;
    @NotNull(message = "User access type must not be null")
    private UserAccessType accessType;
}
