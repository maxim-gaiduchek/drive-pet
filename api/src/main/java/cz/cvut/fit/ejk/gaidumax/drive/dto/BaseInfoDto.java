package cz.cvut.fit.ejk.gaidumax.drive.dto;

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
public class BaseInfoDto {

    @NotNull(message = "Id must not be null")
    private Long id;
    private String name;
}
