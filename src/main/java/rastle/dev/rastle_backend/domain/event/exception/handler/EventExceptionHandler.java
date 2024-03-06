package rastle.dev.rastle_backend.domain.event.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class EventExceptionHandler {
    private void logException(Exception exception) {
        StackTraceElement[] stackTrace = exception.getStackTrace();
        log.warn(exception.getMessage(), stackTrace[0]);
    }

    public static class NotEventProductException extends RuntimeException {
        public NotEventProductException() {
            super("해당 상품은 이벤트 상품이 아닙니다.");
        }
    }

    public static class AlreadyAppliedException extends RuntimeException {
        public AlreadyAppliedException() {
            super("이미 응모한 상품입니다.");
        }
    }
}
