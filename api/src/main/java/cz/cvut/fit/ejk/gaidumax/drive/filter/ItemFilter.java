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
public class ItemFilter extends UuidBaseFilter {

    private List<ItemType> types;
    private UUID parentFolderId;

    {
        sortVariants.put("name", List.of("name"));
        sortVariants.put("size", List.of("size", "name"));
    }
}
