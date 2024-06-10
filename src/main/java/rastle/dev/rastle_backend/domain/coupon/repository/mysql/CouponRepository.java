package rastle.dev.rastle_backend.domain.coupon.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.coupon.dto.CouponInfo;
import rastle.dev.rastle_backend.domain.coupon.model.Coupon;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    @Query("SELECT new rastle.dev.rastle_backend.domain.coupon.dto.CouponInfo(c.id, c.name, c.discount) " +
        "FROM Coupon c WHERE c.member.id = :currentMemberId AND c.couponStatus='NOT_USED'")
    List<CouponInfo> findByMemberId(@Param("currentMemberId") Long currentMemberId);

    @Query("SELECT new rastle.dev.rastle_backend.domain.coupon.dto.CouponInfo(c.id, c.name, c.discount) " +
        "FROM Coupon c WHERE c.id = :couponId")
    Optional<CouponInfo> findByCouponInfoById(@Param("couponId") Long couponId);

    @Modifying
    @Query("UPDATE Coupon c SET c.couponStatus=rastle.dev.rastle_backend.global.common.enums.CouponStatus.USED WHERE c.id=:id")
    void updateCouponUsed(@Param("id") Long id);
}
