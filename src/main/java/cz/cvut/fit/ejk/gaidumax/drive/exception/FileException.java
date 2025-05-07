package cz.cvut.fit.ejk.gaidumax.drive.exception;

import cz.cvut.fit.ejk.gaidumax.drive.exception.code.ExceptionCode;
import jakarta.ws.rs.core.Response;

public class FileException extends AbstractException {

    public FileException(ExceptionCode exceptionCode) {
        super(Response.Status.INTERNAL_SERVER_ERROR, exceptionCode);
    }

    public FileException(ExceptionCode exceptionCode, Object... formatArgs) {
        super(Response.Status.INTERNAL_SERVER_ERROR, exceptionCode, formatArgs);
    }

    public FileException(String code, String description) {
        super(Response.Status.INTERNAL_SERVER_ERROR, code, description);
    }
}
