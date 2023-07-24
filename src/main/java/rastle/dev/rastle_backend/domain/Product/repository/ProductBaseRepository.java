package rastle.dev.rastle_backend.domain.Product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rastle.dev.rastle_backend.domain.Product.model.ProductBase;

public interface ProductBaseRepository extends JpaRepository<ProductBase, Long> {
}
