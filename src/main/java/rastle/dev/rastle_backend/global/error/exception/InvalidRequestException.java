package rastle.dev.rastle_backend.global.error.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class InvalidRequestException extends GlobalException {
    public InvalidRequestException() {
        super("유효하지 않은 요청으로 인해 에러가 발생했습니다.", BAD_REQUEST);
    }

    public InvalidRequestException(String s) {
        super(s, BAD_REQUEST);
    }
}
