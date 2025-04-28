package cz.cvut.fit.ejk.gaidumax.drive.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ErrorDto {

    private String status;
    private LocalDateTime timestamp;
    private String code;
    private String description;
}
