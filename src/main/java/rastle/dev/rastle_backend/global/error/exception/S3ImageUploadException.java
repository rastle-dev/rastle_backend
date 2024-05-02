package rastle.dev.rastle_backend.global.error.exception;

import static org.springframework.http.HttpStatus.CONFLICT;

public class S3ImageUploadException extends GlobalException {
    public S3ImageUploadException() {
        super("S3에 이미지를 올리는 과정에서 예외가 발생하였습니다", CONFLICT);
    }

    public S3ImageUploadException(String s) {
        super(s, CONFLICT);
    }
}

