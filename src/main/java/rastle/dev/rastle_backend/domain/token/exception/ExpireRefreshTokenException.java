package rastle.dev.rastle_backend.domain.token.exception;

public class ExpireRefreshTokenException extends IllegalArgumentException {
    public ExpireRefreshTokenException() {
        super("만료된 리프레쉬 토큰 입니다");
    }

    public ExpireRefreshTokenException(String s) {
        super(s);
    }
}
