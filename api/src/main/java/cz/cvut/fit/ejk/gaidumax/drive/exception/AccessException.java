package cz.cvut.fit.ejk.gaidumax.drive.exception;

import cz.cvut.fit.ejk.gaidumax.drive.exception.code.ExceptionCode;
import jakarta.ws.rs.core.Response;

public class AccessException extends AbstractException{

    public AccessException(ExceptionCode exceptionCode) {
        super(Response.Status.FORBIDDEN, exceptionCode);
    }

    public AccessException(ExceptionCode exceptionCode, Object... formatArgs) {
        super(Response.Status.FORBIDDEN, exceptionCode, formatArgs);
    }

    public AccessException(String code, String description) {
        super(Response.Status.FORBIDDEN, code, description);
    }
}
