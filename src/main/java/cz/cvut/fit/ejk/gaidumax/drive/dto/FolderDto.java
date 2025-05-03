package cz.cvut.fit.ejk.gaidumax.drive.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FolderDto {

    private UUID id;
    @NotBlank(message = "Folder name must not be blank")
    private String name;
    @Valid
    private UuidBaseInfoDto parentFolder;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
