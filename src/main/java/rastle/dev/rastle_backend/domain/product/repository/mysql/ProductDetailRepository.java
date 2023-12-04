package rastle.dev.rastle_backend.domain.product.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import rastle.dev.rastle_backend.domain.product.model.ProductDetail;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
}
