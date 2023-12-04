package rastle.dev.rastle_backend.domain.payment.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import rastle.dev.rastle_backend.domain.payment.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
