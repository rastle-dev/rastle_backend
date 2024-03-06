package rastle.dev.rastle_backend.domain.bundle.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BundleExceptionHandler {

    private void logException(Exception exception) {
        StackTraceElement[] stackTrace = exception.getStackTrace();
        log.warn(exception.getMessage(), stackTrace[0]);
    }
}
