package rastle.dev.rastle_backend.domain.payment.exception;

public class PaymentErrorException extends PaymentException {
    private final String errorCode;

    public PaymentErrorException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
