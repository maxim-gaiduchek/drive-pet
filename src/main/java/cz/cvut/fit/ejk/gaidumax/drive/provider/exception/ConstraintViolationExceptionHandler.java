package cz.cvut.fit.ejk.gaidumax.drive.provider.exception;

import cz.cvut.fit.ejk.gaidumax.drive.dto.exception.ErrorDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.exception.ValidationErrorDto;
import cz.cvut.fit.ejk.gaidumax.drive.exception.code.ExceptionCode;
import cz.cvut.fit.ejk.gaidumax.drive.exception.code.ValidationExceptionCode;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.SetUtils;

import java.time.LocalDateTime;
import java.util.List;

@Provider
@Slf4j
public class ConstraintViolationExceptionHandler implements ExceptionMapper<ConstraintViolationException> {

    private static final Response.Status STATUS = Response.Status.BAD_REQUEST;
    private static final ExceptionCode EXCEPTION_CODE = ValidationExceptionCode.INVALID_DTO;
    private static final String STATUS_CODE_TEMPLATE = "%d %s";

    @Override
    public Response toResponse(ConstraintViolationException e) {
        var errorDto = buildErrorDto(e);
        return Response.status(STATUS)
                .entity(errorDto)
                .build();
    }

    private ErrorDto buildErrorDto(ConstraintViolationException e) {
        var formattedStatusCode = formatStatusCode();
        var errors = buildValidationErrors(e);
        return ErrorDto.builder()
                .status(formattedStatusCode)
                .code(EXCEPTION_CODE.getCode())
                .description(EXCEPTION_CODE.getDescription())
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private List<ValidationErrorDto> buildValidationErrors(ConstraintViolationException e) {
        return SetUtils.emptyIfNull(e.getConstraintViolations()).stream()
                .map(violation -> ValidationErrorDto.builder()
                        .field(violation.getPropertyPath().toString())
                        .message(violation.getMessage())
                        .build())
                .toList();
    }

    private String formatStatusCode() {
        return STATUS_CODE_TEMPLATE.formatted(STATUS.getStatusCode(), STATUS.getReasonPhrase());
    }
}
