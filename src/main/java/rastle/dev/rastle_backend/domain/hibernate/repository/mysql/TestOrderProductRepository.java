package rastle.dev.rastle_backend.domain.hibernate.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import rastle.dev.rastle_backend.domain.hibernate.model.TestOrderProduct;

public interface TestOrderProductRepository extends JpaRepository<TestOrderProduct, Long>{

}
