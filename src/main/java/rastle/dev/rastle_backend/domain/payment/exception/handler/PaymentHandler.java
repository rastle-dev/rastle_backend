package rastle.dev.rastle_backend.domain.payment.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import rastle.dev.rastle_backend.domain.payment.exception.PaymentException;
import rastle.dev.rastle_backend.global.error.response.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class PaymentHandler {
    @ExceptionHandler(PaymentException.class)
    protected final ResponseEntity<ErrorResponse> handlePaymentException(
            PaymentException ex, WebRequest request
    ) {
        log.info(ex.getMessage());
        return new ResponseEntity<>(ErrorResponse
                .builder()
                .errorCode(409L)
                .message(ex.getMessage())
                .build(), HttpStatus.CONFLICT);
    }
}
