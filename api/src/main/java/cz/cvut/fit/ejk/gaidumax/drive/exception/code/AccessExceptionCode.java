package cz.cvut.fit.ejk.gaidumax.drive.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AccessExceptionCode implements ExceptionCode {

    TOKEN_INVALID("API-AUTH-001", "Invalid JWT"),
    TOKEN_EXPIRED("API-AUTH-002", "JWT is expired"),
    TOKEN_REQUIRED("API-AUTH-003", "A JWT token is required to access this resource"),
    ACCESS_DENIED("API-AUTH-004", "User does not have access to this resource"),
    INVALID_LOGIN_OR_PASSWORD("API-AUTH-005", "Invalid login or password"),
    ;

    public final String code;
    public final String description;
}
