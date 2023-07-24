package rastle.dev.rastle_backend.domain.Orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rastle.dev.rastle_backend.domain.Orders.model.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long> {
}
