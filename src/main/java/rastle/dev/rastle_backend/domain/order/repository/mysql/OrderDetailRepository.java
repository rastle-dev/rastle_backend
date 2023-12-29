package rastle.dev.rastle_backend.domain.order.repository.mysql;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.order.model.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByMemberId(Long memberId);

    @Query("SELECT od FROM OrderDetail od JOIN FETCH od.orderProduct WHERE od.orderNumber = :orderNumber")
    Optional<OrderDetail> findByOrderNumber(@Param("orderNumber") String orderNumber);

    @Query("DELETE FROM OrderDetail od WHERE od.paymentStatus = 'READY'")
    void deleteAllReadyOrders();
}
