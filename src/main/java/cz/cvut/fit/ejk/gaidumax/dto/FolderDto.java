package cz.cvut.fit.ejk.gaidumax.dto;

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
public class FolderDto {

    private Long id;
    private String name;
    private BaseInfoDto parentFolder;
}
