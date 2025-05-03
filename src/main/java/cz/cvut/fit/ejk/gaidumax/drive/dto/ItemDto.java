package cz.cvut.fit.ejk.gaidumax.drive.dto;

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
public class ItemDto {

    private UUID id;
    private String name;
    private String type;
    private Long size;
    private UUID parentFolderId;
}
