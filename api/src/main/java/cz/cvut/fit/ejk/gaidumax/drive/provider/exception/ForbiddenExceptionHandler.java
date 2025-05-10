package cz.cvut.fit.ejk.gaidumax.drive.provider.exception;

import cz.cvut.fit.ejk.gaidumax.drive.dto.exception.ErrorDto;
import cz.cvut.fit.ejk.gaidumax.drive.exception.code.AccessExceptionCode;
import cz.cvut.fit.ejk.gaidumax.drive.exception.code.ExceptionCode;
import io.quarkus.security.ForbiddenException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;

@Provider
public class ForbiddenExceptionHandler implements ExceptionMapper<ForbiddenException> {

    private static final Response.Status STATUS = Response.Status.FORBIDDEN;
    private static final ExceptionCode EXCEPTION_CODE = AccessExceptionCode.ACCESS_DENIED;
    private static final String STATUS_CODE_TEMPLATE = "%d %s";

    @Override
    public Response toResponse(ForbiddenException e) {
        var errorDto = buildErrorDto();
        return Response.status(STATUS)
                .entity(errorDto)
                .build();
    }

    private ErrorDto buildErrorDto() {
        var formattedStatusCode = formatStatusCode();
        return ErrorDto.builder()
                .status(formattedStatusCode)
                .code(EXCEPTION_CODE.getCode())
                .description(EXCEPTION_CODE.getDescription())
                .timestamp(LocalDateTime.now())
                .build();
    }

    private String formatStatusCode() {
        return STATUS_CODE_TEMPLATE.formatted(STATUS.getStatusCode(), STATUS.getReasonPhrase());
    }
}
