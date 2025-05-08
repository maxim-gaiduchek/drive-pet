package cz.cvut.fit.ejk.gaidumax.drive.dto;

import cz.cvut.fit.ejk.gaidumax.drive.entity.ItemType;
import jakarta.ws.rs.QueryParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ItemSearchDto extends PageableDto {

    @QueryParam("types")
    private List<ItemType> types;
    @QueryParam("parentFolderId")
    private UUID parentFolderId;
}
