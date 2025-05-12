package cz.cvut.fit.ejk.gaidumax.drive.dto;

import cz.cvut.fit.ejk.gaidumax.drive.entity.ItemType;
import cz.cvut.fit.ejk.gaidumax.drive.entity.UserAccessType;
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
public class ItemDto {

    private UUID id;
    private ItemType type;
    private String name;
    private String fileType;
    private String path;
    private Long size;
    private UserDto owner;
    private FolderDto parentFolder;
    private UserAccessType userAccessType;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
