package cz.cvut.fit.ejk.gaidumax.drive.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FileExceptionCode implements ExceptionCode {

    FILE_DOES_NOT_EXIST("API-FILE-001", "File with id %s does not exist"),
    FILE_UPLOAD_ERROR("API-FILE-002", "File upload error"),
    ;

    private final String code;
    private final String description;
}
