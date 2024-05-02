package rastle.dev.rastle_backend.domain.member.exception;

import rastle.dev.rastle_backend.global.error.exception.GlobalException;

import static org.springframework.http.HttpStatus.CONFLICT;

public class LogoutMemberException extends GlobalException {
    public LogoutMemberException() {
        super("이미 로그아웃한 유저입니다", CONFLICT);
    }

    public LogoutMemberException(String s) {
        super(s, CONFLICT);
    }
}
