package cz.cvut.fit.ejk.gaidumax.drive.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ValidationExceptionCode implements ExceptionCode {

    INVALID_DTO("API-VLD-001", "Invalid DTO"),
    ;

    private final String code;
    private final String description;
}
