package cz.cvut.fit.ejk.gaidumax.drive.dto;

import jakarta.validation.Valid;
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
public class FileDto {

    private UUID id;
    private String fileName;
    private String fileType;
    private Long size;
    private String filePath;
    @Valid
    private UuidBaseInfoDto parentFolder;
    private UserDto author;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private String accessToken;
}
