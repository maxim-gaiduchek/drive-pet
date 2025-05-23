package cz.cvut.fit.ejk.gaidumax.drive.dto.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDto {

    private String status;
    private String code;
    private String description;
    private List<ValidationErrorDto> errors;
    private LocalDateTime timestamp;
}
