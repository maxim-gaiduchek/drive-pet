package cz.cvut.fit.ejk.gaidumax.drive.dto;

import jakarta.ws.rs.QueryParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class PageableDto {

    @QueryParam("page")
    @Builder.Default
    private Integer page = 1;
    @QueryParam("pageSize")
    @Builder.Default
    private Integer pageSize = 20;
    @QueryParam("sortBy")
    @Builder.Default
    private String sortBy = "createdAt";
    @QueryParam("sortDirection")
    @Builder.Default
    private String sortDirection = "desc";
}
