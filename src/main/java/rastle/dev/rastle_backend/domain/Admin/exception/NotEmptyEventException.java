package rastle.dev.rastle_backend.domain.Admin.exception;

public class NotEmptyEventException extends IllegalArgumentException {
    public NotEmptyEventException() {
        super("이벤트에 속한 상품이 있어서 삭제 불가");
    }

    public NotEmptyEventException(String s) {
        super(s);
    }
}
