package rastle.dev.rastle_backend.domain.order.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import rastle.dev.rastle_backend.domain.order.model.OrderProduct;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
