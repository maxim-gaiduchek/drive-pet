package cz.cvut.fit.ejk.gaidumax.drive.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FileExceptionCode implements ExceptionCode {

    FILE_DOES_NOT_EXIST("API-FILE-001", "File with id %s does not exist"),
    FILE_UPLOAD_ERROR("API-FILE-002", "File upload error"),
    USER_HAS_NO_ACCESS_TO_FILE("API-FILE-003", "User with id %d has no access to the file with id %s"),
    INVALID_USER_ACCESS_TYPE("API-FILE-004", "User access type %s is not permitted for create/update access. Permitted: %s"),
    ;

    private final String code;
    private final String description;
}
