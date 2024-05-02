package rastle.dev.rastle_backend.global.error.exception.port_one;

import rastle.dev.rastle_backend.global.error.exception.GlobalException;

import static org.springframework.http.HttpStatus.CONFLICT;

public class PortOneException extends GlobalException {
    public PortOneException() {
        super("포트원 API를 사용하는 과정에서 오류가 발생했습니다.", CONFLICT);
    }

    public PortOneException(String s) {
        super(s, CONFLICT);
    }
}
