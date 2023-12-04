package rastle.dev.rastle_backend.domain.order.repository.mysql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import rastle.dev.rastle_backend.domain.order.model.MemberOrder;

public interface MemberOrderRepository extends JpaRepository<MemberOrder, Long> {
    List<MemberOrder> findByMemberId(Long memberId);
}
