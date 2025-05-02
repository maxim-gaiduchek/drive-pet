package cz.cvut.fit.ejk.gaidumax.drive.exception;

import cz.cvut.fit.ejk.gaidumax.drive.exception.code.ExceptionCode;
import jakarta.ws.rs.core.Response;

public class ValidationException extends AbstractException {

    public ValidationException(ExceptionCode exceptionCode) {
        super(Response.Status.BAD_REQUEST, exceptionCode);
    }

    public ValidationException(ExceptionCode exceptionCode, Object... formatArgs) {
        super(Response.Status.BAD_REQUEST, exceptionCode, formatArgs);
    }

    public ValidationException(String code, String description) {
        super(Response.Status.BAD_REQUEST, code, description);
    }
}
