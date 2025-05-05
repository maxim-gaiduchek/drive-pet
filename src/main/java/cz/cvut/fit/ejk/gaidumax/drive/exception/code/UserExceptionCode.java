package cz.cvut.fit.ejk.gaidumax.drive.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserExceptionCode implements ExceptionCode {

    USER_DOES_NOT_EXIST("API-USER-001", "User with id %d does not exist"),
    EMAIL_IS_ALREADY_EXISTS("API-USER-002", "User email '%s' already exists"),
    ;

    private final String code;
    private final String description;
}
