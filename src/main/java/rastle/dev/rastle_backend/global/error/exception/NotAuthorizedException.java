package rastle.dev.rastle_backend.global.error.exception;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class NotAuthorizedException extends GlobalException {
    public NotAuthorizedException() {
        super("접근 권한이 없습니다.", FORBIDDEN);

    }

    public NotAuthorizedException(String s) {
        super(s, FORBIDDEN);
    }
}
