package rastle.dev.rastle_backend.domain.Orders.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import rastle.dev.rastle_backend.domain.Orders.model.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByMemberId(Long memberId);
}
