package cz.cvut.fit.ejk.gaidumax.drive.dto;

import jakarta.validation.Valid;
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
public class UpdateFileDto {

    @NotBlank(message = "File name must not be neither empty nor blank")
    private String fileName;
    @Valid
    private UuidBaseInfoDto parentFolder;
}
