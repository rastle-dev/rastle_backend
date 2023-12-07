package rastle.dev.rastle_backend.domain.order.repository.mysql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import rastle.dev.rastle_backend.domain.order.model.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByMemberId(Long memberId);
}
