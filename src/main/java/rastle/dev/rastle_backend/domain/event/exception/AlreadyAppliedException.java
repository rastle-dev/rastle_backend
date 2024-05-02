package rastle.dev.rastle_backend.domain.event.exception;

import rastle.dev.rastle_backend.global.error.exception.GlobalException;

import static org.springframework.http.HttpStatus.CONFLICT;

public class AlreadyAppliedException extends GlobalException {
    public AlreadyAppliedException() {
        super("이미 응모한 상품입니다.", CONFLICT);
    }
}
