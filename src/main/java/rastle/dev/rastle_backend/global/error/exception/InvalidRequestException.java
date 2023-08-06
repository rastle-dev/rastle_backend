package rastle.dev.rastle_backend.global.error.exception;

public class InvalidRequestException extends IllegalArgumentException {
    public InvalidRequestException() {
        super("유효하지 않은 요청으로 인해 에러가 발생했습니다.");
    }

    public InvalidRequestException(String s) {
        super(s);
    }
}
