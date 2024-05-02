package rastle.dev.rastle_backend.domain.payment.exception;

import rastle.dev.rastle_backend.global.error.exception.GlobalException;

import static org.springframework.http.HttpStatus.CONFLICT;

public class PaymentException extends GlobalException {
    public PaymentException() {
        super("결제 과정에서 오류가 발생했습니다.", CONFLICT);
    }

    public PaymentException(String s) {
        super(s, CONFLICT);
    }
}
