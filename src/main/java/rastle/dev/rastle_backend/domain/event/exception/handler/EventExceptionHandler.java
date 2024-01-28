package rastle.dev.rastle_backend.domain.event.exception.handler;

public class EventExceptionHandler {
    public static class NotEventProductException extends RuntimeException {
        public NotEventProductException() {
            super("해당 상품은 이벤트 상품이 아닙니다.");
        }
    }
}
