package rastle.dev.rastle_backend.domain.admin.exception;

import rastle.dev.rastle_backend.global.error.exception.GlobalException;

import static org.springframework.http.HttpStatus.CONFLICT;

public class NotEmptyCategoryException extends GlobalException {
    public NotEmptyCategoryException() {
        super("카테고리에 속한 상품이 있어서 삭제불가", CONFLICT);
    }

    public NotEmptyCategoryException(String s) {
        super(s, CONFLICT);
    }
}
