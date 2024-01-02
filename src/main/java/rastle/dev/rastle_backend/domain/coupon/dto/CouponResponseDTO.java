package rastle.dev.rastle_backend.domain.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CouponResponseDTO {
    List<CouponInfo> couponInfos;
}
