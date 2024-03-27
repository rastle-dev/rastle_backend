package rastle.dev.rastle_backend.domain.admin.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rastle.dev.rastle_backend.domain.admin.exception.InvalidImageUrlException;
import rastle.dev.rastle_backend.domain.admin.exception.NotEmptyBundleException;
import rastle.dev.rastle_backend.domain.admin.exception.NotEmptyCategoryException;
import rastle.dev.rastle_backend.domain.admin.exception.NotEmptyEventException;
import rastle.dev.rastle_backend.global.error.exception.S3ImageUploadException;
import rastle.dev.rastle_backend.global.error.response.ErrorResponse;

import static org.springframework.http.HttpStatus.CONFLICT;

@Slf4j
@RestControllerAdvice
public class AdminExceptionHandler {

    private void logException(Exception exception, HttpServletRequest webRequest) {
        log.warn("{} {}", webRequest.getMethod(), webRequest.getRequestURI());

        StackTraceElement[] stackTrace = exception.getStackTrace();
        log.warn(exception.getMessage(), stackTrace[0]);
    }

    @ExceptionHandler(InvalidImageUrlException.class)
    protected final ResponseEntity<ErrorResponse> handleS3ImageException(
        S3ImageUploadException ex, HttpServletRequest request) {
        logException(ex, request);
        return new ResponseEntity<>(ErrorResponse.builder()
            .errorCode(409L)
            .message(ex.getMessage())
            .build(),
            CONFLICT);
    }

    @ExceptionHandler(NotEmptyBundleException.class)
    protected final ResponseEntity<ErrorResponse> handleNotEmptyBundleException(
        NotEmptyBundleException ex, HttpServletRequest request
    ) {
        logException(ex, request);
        return new ResponseEntity<>(ErrorResponse.builder()
            .errorCode(409L)
            .message(ex.getMessage())
            .build(),
            CONFLICT);
    }

    @ExceptionHandler(NotEmptyCategoryException.class)
    protected final ResponseEntity<ErrorResponse> handleNotEmptyCategoryException(
        NotEmptyCategoryException ex, HttpServletRequest request
    ) {
        logException(ex, request);
        return new ResponseEntity<>(ErrorResponse.builder()
            .errorCode(409L)
            .message(ex.getMessage())
            .build(),
            CONFLICT);
    }

    @ExceptionHandler(NotEmptyEventException.class)
    protected final ResponseEntity<ErrorResponse> handleNotEmptyEventException(
        NotEmptyEventException ex, HttpServletRequest request
    ) {
        logException(ex, request);
        return new ResponseEntity<>(ErrorResponse.builder()
            .errorCode(409L)
            .message(ex.getMessage())
            .build(),
            CONFLICT);
    }

}
