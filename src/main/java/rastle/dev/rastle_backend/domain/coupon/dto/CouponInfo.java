package rastle.dev.rastle_backend.domain.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponInfo {
    private Long id;
    private String name;
    private int discount;
    ;

}
