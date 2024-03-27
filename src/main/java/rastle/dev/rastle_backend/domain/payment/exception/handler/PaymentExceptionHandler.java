package rastle.dev.rastle_backend.domain.payment.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rastle.dev.rastle_backend.domain.payment.exception.PaymentException;
import rastle.dev.rastle_backend.global.error.response.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class PaymentExceptionHandler {
    private void logException(Exception exception, HttpServletRequest request) {
        log.warn("{} {}", request.getMethod(), request.getRequestURI());

        StackTraceElement[] stackTrace = exception.getStackTrace();
        log.warn(exception.getClass().getName(), stackTrace[0]);
        log.warn(exception.getMessage(), stackTrace[0]);

    }

    @ExceptionHandler(PaymentException.class)
    protected final ResponseEntity<ErrorResponse> handlePaymentException(
        PaymentException ex, HttpServletRequest request
    ) {
        logException(ex, request);
        return new ResponseEntity<>(ErrorResponse
            .builder()
            .errorCode(409L)
            .message(ex.getMessage())
            .build(), HttpStatus.CONFLICT);
    }
}
