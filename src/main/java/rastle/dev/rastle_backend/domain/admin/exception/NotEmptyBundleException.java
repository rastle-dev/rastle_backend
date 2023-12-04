package rastle.dev.rastle_backend.domain.admin.exception;

public class NotEmptyBundleException extends IllegalArgumentException {
    public NotEmptyBundleException() {
        super("상품 세트에 속한 상품이 있어서 삭제 불가");
    }

    public NotEmptyBundleException(String s) {
        super(s);
    }
}
