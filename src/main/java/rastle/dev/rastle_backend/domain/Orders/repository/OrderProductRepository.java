package rastle.dev.rastle_backend.domain.Orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rastle.dev.rastle_backend.domain.Orders.model.OrderProduct;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
