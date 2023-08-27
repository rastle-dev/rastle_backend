package rastle.dev.rastle_backend.domain.Event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rastle.dev.rastle_backend.domain.Product.model.EventProduct;

public interface EventProductRepository extends JpaRepository<EventProduct, Long> {
}
