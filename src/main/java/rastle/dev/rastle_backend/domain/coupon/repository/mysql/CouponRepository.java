package rastle.dev.rastle_backend.domain.coupon.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.coupon.dto.CouponInfo;
import rastle.dev.rastle_backend.domain.coupon.model.Coupon;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    @Query("SELECT new rastle.dev.rastle_backend.domain.coupon.dto.CouponInfo(c.id, c.name, c.discount) " +
        "FROM Coupon c WHERE c.member.id = :currentMemberId")
    List<CouponInfo> findByMemberId(@Param("currentMemberId") Long currentMemberId);
}
