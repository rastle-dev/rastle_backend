package rastle.dev.rastle_backend.domain.hibernate.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import rastle.dev.rastle_backend.domain.hibernate.model.TestDelivery;

public interface TestDeliveryRepository extends JpaRepository<TestDelivery, Long>{

}
