package cz.cvut.fit.ejk.gaidumax.drive.repository.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PageRequest {

    @Builder.Default
    private int page = 0;
    private int pageSize;

    public int getOffset() {
        return page * pageSize;
    }

    public static PageRequest of(int pageSize) {
        return new PageRequest(0, pageSize);
    }

    public static PageRequest of(int page, int pageSize) {
        return new PageRequest(page, pageSize);
    }
}
