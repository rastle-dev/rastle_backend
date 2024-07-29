package rastle.dev.rastle_backend.domain.hibernate.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import rastle.dev.rastle_backend.domain.hibernate.model.TestPayment;

public interface TestPaymentRepository extends JpaRepository<TestPayment, Long>{

}
