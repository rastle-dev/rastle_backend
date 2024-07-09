package rastle.dev.rastle_backend.domain.hibernate.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import rastle.dev.rastle_backend.domain.hibernate.model.TestOrder;

public interface TestOrderRepository extends JpaRepository<TestOrder, Long>{

}
