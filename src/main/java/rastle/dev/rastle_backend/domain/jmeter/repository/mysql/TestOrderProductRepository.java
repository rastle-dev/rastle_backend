package rastle.dev.rastle_backend.domain.jmeter.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import rastle.dev.rastle_backend.domain.jmeter.model.TestOrderProduct;

public interface TestOrderProductRepository extends JpaRepository<TestOrderProduct, Long> {
}
