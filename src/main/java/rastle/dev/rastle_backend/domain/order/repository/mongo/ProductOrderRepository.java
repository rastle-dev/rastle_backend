package rastle.dev.rastle_backend.domain.order.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import rastle.dev.rastle_backend.domain.order.model.ProductOrder;

public interface ProductOrderRepository extends MongoRepository<ProductOrder, String> {
}
