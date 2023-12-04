package rastle.dev.rastle_backend.domain.admin.exception;

public class NotEmptyCategoryException extends IllegalArgumentException {
    public NotEmptyCategoryException() {
        super("카테고리에 속한 상품이 있어서 삭제불가");
    }

    public NotEmptyCategoryException(String s) {
        super(s);
    }
}
