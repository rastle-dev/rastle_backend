package rastle.dev.rastle_backend.domain.payment.exception;

public class PaymentException extends IllegalArgumentException {
    public PaymentException() {
    }

    public PaymentException(String s) {
        super(s);
    }
}
