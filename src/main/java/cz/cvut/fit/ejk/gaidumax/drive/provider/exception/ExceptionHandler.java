package cz.cvut.fit.ejk.gaidumax.drive.provider.exception;

import cz.cvut.fit.ejk.gaidumax.drive.dto.ErrorDto;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Provider
@Slf4j
public class ExceptionHandler implements ExceptionMapper<Exception> {

    private static final Response.Status STATUS = Response.Status.INTERNAL_SERVER_ERROR;
    private static final String STATUS_CODE_TEMPLATE = "%d %s";

    @Override
    public Response toResponse(Exception e) {
        log.error("Unexpected exception occurred: ", e);
        var errorDto = buildErrorDto();
        return Response.status(STATUS)
                .entity(errorDto)
                .build();
    }

    private ErrorDto buildErrorDto() {
        var formattedStatusCode = formatStatusCode();
        return ErrorDto.builder()
                .status(formattedStatusCode)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private String formatStatusCode() {
        return STATUS_CODE_TEMPLATE.formatted(STATUS.getStatusCode(), STATUS.getReasonPhrase());
    }
}
