package rastle.dev.rastle_backend.domain.coupon.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CouponExceptionHandler {
    private void logException(Exception exception) {
        StackTraceElement[] stackTrace = exception.getStackTrace();
        log.warn(exception.getMessage(), stackTrace[0]);
    }
}
