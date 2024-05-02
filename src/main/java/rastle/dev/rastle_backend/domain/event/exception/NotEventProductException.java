package rastle.dev.rastle_backend.domain.event.exception;

import rastle.dev.rastle_backend.global.error.exception.GlobalException;

import static org.springframework.http.HttpStatus.CONFLICT;

public class NotEventProductException extends GlobalException {
    public NotEventProductException() {
        super("해당 상품은 이벤트 상품이 아닙니다.", CONFLICT);
    }
}
