package cz.cvut.fit.ejk.gaidumax.drive.filter;

import cz.cvut.fit.ejk.gaidumax.drive.repository.data.PageRequest;
import cz.cvut.fit.ejk.gaidumax.drive.repository.data.Sort;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder(builderMethodName = "baseBuilder")
public class UuidBaseFilter {

    protected List<UUID> ids;
    @Builder.Default
    protected Integer page = 1;
    @Builder.Default
    protected Integer pageSize = 20;
    protected String sortBy;
    protected String sortDirection;

    protected final Map<String, List<String>> sortVariants = new HashMap<>();
    protected static final String DEFAULT_SORT_BY = "createdAt";
    protected static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.DESC;

    {
        sortVariants.put("id", List.of("id"));
        sortVariants.put("createdAt", List.of("createdAt"));
    }

    public PageRequest buildPageable() {
        if (pageSize == 0) {
            return PageRequest.of(0, Integer.MAX_VALUE);
        }
        return PageRequest.of(page - 1, pageSize);
    }

    public Sort buildSort() {
        var direction = getSortDirection();
        var property = sortVariants.getOrDefault(sortBy, List.of(DEFAULT_SORT_BY)).toArray(new String[0]);
        return Sort.by(property, direction);
    }

    protected Sort.Direction getSortDirection() {
        return Optional.ofNullable(Sort.Direction.parse(sortDirection))
                .orElse(DEFAULT_SORT_DIRECTION);
    }
}
