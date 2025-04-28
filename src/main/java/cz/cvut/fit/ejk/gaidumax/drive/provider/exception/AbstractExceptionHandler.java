package cz.cvut.fit.ejk.gaidumax.drive.provider.exception;

import cz.cvut.fit.ejk.gaidumax.drive.dto.ErrorDto;
import cz.cvut.fit.ejk.gaidumax.drive.exception.AbstractException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;

@Provider
public class AbstractExceptionHandler implements ExceptionMapper<AbstractException> {

    private static final String STATUS_CODE_TEMPLATE = "%d %s";

    @Override
    public Response toResponse(AbstractException e) {
        var errorDto = buildErrorDto(e);
        return Response.status(e.getStatus())
                .entity(errorDto)
                .build();
    }

    private ErrorDto buildErrorDto(AbstractException e) {
        var status = e.getStatus();
        var formattedStatusCode = formatStatusCode(status);
        return ErrorDto.builder()
                .status(formattedStatusCode)
                .timestamp(LocalDateTime.now())
                .code(e.getCode())
                .description(e.getDescription())
                .build();
    }

    private String formatStatusCode(Response.Status status) {
        return STATUS_CODE_TEMPLATE.formatted(status.getStatusCode(), status.getReasonPhrase());
    }
}
