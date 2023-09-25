package rastle.dev.rastle_backend.domain.Admin.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import rastle.dev.rastle_backend.domain.Admin.exception.InvalidImageUrlException;
import rastle.dev.rastle_backend.global.error.exception.S3ImageUploadException;
import rastle.dev.rastle_backend.global.error.response.ErrorResponse;

import static org.springframework.http.HttpStatus.CONFLICT;

@Slf4j
@RestControllerAdvice
public class AdminExceptionHandler {
    @ExceptionHandler(InvalidImageUrlException.class)
    protected final ResponseEntity<ErrorResponse> handleS3ImageException(
            S3ImageUploadException ex, WebRequest request) {
        log.warn(ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorCode(409L)
                .message(ex.getMessage())
                .build(),
                CONFLICT);
    }
}
