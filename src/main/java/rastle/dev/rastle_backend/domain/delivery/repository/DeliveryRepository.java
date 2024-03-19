package rastle.dev.rastle_backend.domain.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rastle.dev.rastle_backend.domain.delivery.model.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
