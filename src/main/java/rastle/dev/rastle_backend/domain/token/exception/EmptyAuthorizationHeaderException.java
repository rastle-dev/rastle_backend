package rastle.dev.rastle_backend.domain.token.exception;

public class EmptyAuthorizationHeaderException extends IllegalArgumentException {
    public EmptyAuthorizationHeaderException() {
        super("인증 관련 헤더 값이 비어있는 요청입니다.");
    }

    public EmptyAuthorizationHeaderException(String s) {
        super(s);
    }
}
