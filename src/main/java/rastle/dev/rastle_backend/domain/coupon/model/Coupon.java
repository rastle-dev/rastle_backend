package rastle.dev.rastle_backend.domain.coupon.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.member.model.Member;
import rastle.dev.rastle_backend.domain.product.model.ProductBase;
import rastle.dev.rastle_backend.global.common.enums.CouponStatus;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "coupon")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;
    @Enumerated(STRING)
    @Column(name = "coupon_status")
    private CouponStatus couponStatus;
    private String name;
    private int discount;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
