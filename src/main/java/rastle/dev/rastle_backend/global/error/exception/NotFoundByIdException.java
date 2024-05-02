package rastle.dev.rastle_backend.global.error.exception;

import static org.springframework.http.HttpStatus.CONFLICT;

public class NotFoundByIdException extends GlobalException {
    public NotFoundByIdException() {
        super("해당 아이디로 존재하는 객체를 찾을 수 없습니다.", CONFLICT);
    }

    public NotFoundByIdException(String s) {
        super(s, CONFLICT);
    }
}