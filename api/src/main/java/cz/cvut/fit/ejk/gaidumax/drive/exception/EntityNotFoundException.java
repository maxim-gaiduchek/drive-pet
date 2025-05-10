package cz.cvut.fit.ejk.gaidumax.drive.exception;

import cz.cvut.fit.ejk.gaidumax.drive.exception.code.ExceptionCode;
import jakarta.ws.rs.core.Response;

public class EntityNotFoundException extends AbstractException {

    public EntityNotFoundException(ExceptionCode exceptionCode) {
        super(Response.Status.NOT_FOUND, exceptionCode);
    }

    public EntityNotFoundException(ExceptionCode exceptionCode, Object... formatArgs) {
        super(Response.Status.NOT_FOUND, exceptionCode, formatArgs);
    }

    public EntityNotFoundException(String code, String description) {
        super(Response.Status.NOT_FOUND, code, description);
    }
}
