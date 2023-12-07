package rastle.dev.rastle_backend.domain.token.exception;

public class ExpireAccessTokenException extends IllegalArgumentException {
    public ExpireAccessTokenException() {
        super("만료된 액세스 토큰으로 온 인증 요청입니다");
    }

    public ExpireAccessTokenException(String s) {
        super(s);
    }
}
