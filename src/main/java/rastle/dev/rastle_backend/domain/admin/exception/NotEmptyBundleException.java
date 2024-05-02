package rastle.dev.rastle_backend.domain.admin.exception;

import rastle.dev.rastle_backend.global.error.exception.GlobalException;

import static org.springframework.http.HttpStatus.CONFLICT;

public class NotEmptyBundleException extends GlobalException {
    public NotEmptyBundleException() {
        super("상품 세트에 속한 상품이 있어서 삭제 불가", CONFLICT);
    }

    public NotEmptyBundleException(String s) {
        super(s, CONFLICT);
    }
}
