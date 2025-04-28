package cz.cvut.fit.ejk.gaidumax.drive.dto;

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
public class BaseInfoDto {

    private Long id;
    private String name;
}
