package rastle.dev.rastle_backend.domain.coupon.exception;

import rastle.dev.rastle_backend.global.error.exception.GlobalException;

import static org.springframework.http.HttpStatus.CONFLICT;

public class AlreadyUsedCouponException extends GlobalException {
    public AlreadyUsedCouponException() {
        super("이미 사용한 쿠폰이므로 재사용이 불가합니다.", CONFLICT);
    }

    public AlreadyUsedCouponException(String s) {
        super(s, CONFLICT);
    }
}
