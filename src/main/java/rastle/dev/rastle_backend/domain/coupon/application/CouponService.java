package rastle.dev.rastle_backend.domain.coupon.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.coupon.dto.CouponResponseDTO;
import rastle.dev.rastle_backend.domain.coupon.repository.mysql.CouponRepository;
import rastle.dev.rastle_backend.global.util.SecurityUtil;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;

    @Transactional(readOnly = true)
    public CouponResponseDTO getMemberCoupons() {
        return new CouponResponseDTO(couponRepository.findByMemberId(SecurityUtil.getCurrentMemberId()));
    }
}
