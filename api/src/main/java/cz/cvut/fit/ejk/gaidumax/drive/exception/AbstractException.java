package cz.cvut.fit.ejk.gaidumax.drive.exception;

import cz.cvut.fit.ejk.gaidumax.drive.exception.code.ExceptionCode;
import jakarta.ws.rs.core.Response;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AbstractException extends RuntimeException {

    private final Response.Status status;
    private final String code;
    private final String description;

    public AbstractException(Response.Status status, ExceptionCode exceptionCode) {
        this.status = status;
        code = exceptionCode.getCode();
        description = exceptionCode.getDescription();
    }

    public AbstractException(Response.Status status, ExceptionCode exceptionCode, Object... formatArgs) {
        this.status = status;
        this.code = exceptionCode.getCode();
        this.description = exceptionCode.getDescription().formatted(formatArgs);
    }

    public AbstractException(Response.Status status, String code, String description) {
        this.status = status;
        this.code = code;
        this.description = description;
    }
}
