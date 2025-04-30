package cz.cvut.fit.ejk.gaidumax.drive.filter;

import cz.cvut.fit.ejk.gaidumax.drive.entity.BaseEntity;
import cz.cvut.fit.ejk.gaidumax.drive.entity.ItemType;
import cz.cvut.fit.ejk.gaidumax.drive.dto.ItemDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.query.SortDirection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ItemFilter {

    private List<Long> authorIds;
    private List<ItemType> types;
    private Long parentFolderId;
    @Builder.Default
    protected Integer page = 1;
    @Builder.Default
    protected Integer pageSize = 20;
    protected String sortBy;
    protected String sortDirection;

    protected final Map<String, List<String>> sortVariants = new HashMap<>();
    protected static final String DEFAULT_SORT_BY = "createdAt";
    protected static final SortDirection DEFAULT_SORT_DIRECTION = SortDirection.DESCENDING;

    {
        sortVariants.put("createdAt", List.of(BaseEntity.Fields.createdAt));
        sortVariants.put("updatedAt", List.of(BaseEntity.Fields.updatedAt));
    }

    /*public Pageable buildPageable() {
        if (pageSize == 0) {
            return PageRequest.of(0, Integer.MAX_VALUE, buildSort());
        }
        return PageRequest.of(page - 1, pageSize, buildSort());
    }

    protected Sort buildSort() {
        var direction = getSortDirection();
        var property = sortVariants.getOrDefault(sortBy, List.of(DEFAULT_SORT_BY)).toArray(new String[0]);
        return Sort.by(direction, property);
    }*/

    protected CriteriaQuery<ItemDto> buildCriteriaQuery(CriteriaBuilder builder, CriteriaQuery query) {
        /*var criteriaQuery = builder.createQuery(ItemProjection.class);
        var fileSubquery = criteriaQuery.subquery(File.class);
        var folderSubquery = criteriaQuery.subquery(Folder.class);*/

        return null;
    }

    protected SortDirection getSortDirection() {
        try {
            return SortDirection.interpret(sortDirection);
        } catch (IllegalArgumentException e) {
            return DEFAULT_SORT_DIRECTION;
        }
    }
}
