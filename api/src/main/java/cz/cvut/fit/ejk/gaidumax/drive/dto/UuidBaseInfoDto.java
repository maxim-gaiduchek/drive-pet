package cz.cvut.fit.ejk.gaidumax.drive.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UuidBaseInfoDto {

    @NotNull(message = "Id must not be null")
    private UUID id;
    private String name;
}
