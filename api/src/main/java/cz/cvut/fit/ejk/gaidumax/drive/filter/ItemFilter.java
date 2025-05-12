package cz.cvut.fit.ejk.gaidumax.drive.filter;

import cz.cvut.fit.ejk.gaidumax.drive.entity.ItemType;
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
public class ItemFilter extends BaseFilter<UUID> {

    private List<ItemType> types;
    private UUID parentFolderId;
    private String name;

    {
        sortVariants.put("name", List.of("name"));
        sortVariants.put("size", List.of("size", "name"));
    }
}
