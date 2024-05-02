package rastle.dev.rastle_backend.domain.admin.exception;

import rastle.dev.rastle_backend.global.error.exception.GlobalException;

import static org.springframework.http.HttpStatus.CONFLICT;

public class InvalidImageUrlException extends GlobalException {
    public InvalidImageUrlException() {
        super("유효하지 않은 이미지 주소로 삭제할 수 없습니다.", CONFLICT);
    }

    public InvalidImageUrlException(String s) {
        super(s,CONFLICT);
    }
}
