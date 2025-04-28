package cz.cvut.fit.ejk.gaidumax.drive.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserExceptionCode implements ExceptionCode {

    USER_DOES_NOT_EXIST("DRIVE-USR-001", "User with id %d does not exist");
    ;

    private final String code;
    private final String description;
}
