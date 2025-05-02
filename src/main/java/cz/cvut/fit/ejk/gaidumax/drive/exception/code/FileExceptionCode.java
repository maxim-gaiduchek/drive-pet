package cz.cvut.fit.ejk.gaidumax.drive.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FileExceptionCode implements ExceptionCode {

    FILE_DOES_NOT_EXIST("API-FILE-001", "File with id %d does not exist"),
    ;

    private final String code;
    private final String description;
}
