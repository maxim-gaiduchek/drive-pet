package cz.cvut.fit.ejk.gaidumax.drive.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
public class UpdateFileDto {

    @NotBlank(message = "File name must not be neither empty nor blank")
    private String fileName;
    @NotNull(message = "File parent must not be null")
    @Valid
    private BaseInfoDto parentFolder;
}
