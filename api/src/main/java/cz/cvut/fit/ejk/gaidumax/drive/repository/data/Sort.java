package cz.cvut.fit.ejk.gaidumax.drive.repository.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Sort {

    private String[] sortBy;
    @Builder.Default
    private Direction sortDirection = Direction.ASC;

    public static Sort by(String sortBy) {
        return new Sort(new String[]{sortBy}, Direction.ASC);
    }

    public static Sort by(String sortBy, Direction sortDirection) {
        return new Sort(new String[]{sortBy}, sortDirection);
    }

    public static Sort by(String[] sortBy) {
        return new Sort(sortBy, Direction.ASC);
    }

    public static Sort by(String[] sortBy, Direction sortDirection) {
        return new Sort(sortBy, sortDirection);
    }

    @RequiredArgsConstructor
    @Getter
    public enum Direction {
        ASC("asc", Set.of("asc", "ascending")),
        DESC("desc", Set.of("desc", "descending"));

        private final String sql;
        private final Set<String> possibleVariants;

        public static Direction parse(String directionStr) {
            return Arrays.stream(values())
                    .filter(value -> value.possibleVariants.stream()
                            .anyMatch(variant -> variant.equalsIgnoreCase(directionStr)))
                    .findAny()
                    .orElse(null);
        }
    }
}
