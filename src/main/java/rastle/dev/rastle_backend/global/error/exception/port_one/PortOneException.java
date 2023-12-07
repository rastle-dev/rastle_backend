package rastle.dev.rastle_backend.global.error.exception.port_one;

public class PortOneException extends IllegalArgumentException {
    public PortOneException() {
        super("포트원 API를 사용하는 과정에서 오류가 발생했습니다.");
    }

    public PortOneException(String s) {
        super(s);
    }
}
