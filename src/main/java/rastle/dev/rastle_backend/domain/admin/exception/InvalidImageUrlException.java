package rastle.dev.rastle_backend.domain.admin.exception;

public class InvalidImageUrlException extends IllegalArgumentException {
    public InvalidImageUrlException() {
        super("유효하지 않은 이미지 주소로 삭제할 수 없습니다.");
    }

    public InvalidImageUrlException(String s) {
        super(s);
    }
}
