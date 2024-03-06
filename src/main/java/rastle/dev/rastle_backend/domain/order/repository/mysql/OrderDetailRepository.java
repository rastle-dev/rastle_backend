package rastle.dev.rastle_backend.domain.order.repository.mysql;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.order.dto.OrderSimpleInfo;
import rastle.dev.rastle_backend.domain.order.model.OrderDetail;

import java.util.List;
import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByMemberId(Long memberId);

    @Query("SELECT od FROM OrderDetail od JOIN FETCH od.orderProduct WHERE od.orderNumber = :orderNumber")
    Optional<OrderDetail> findByOrderNumber(@Param("orderNumber") String orderNumber);

    @Modifying
    @Query("DELETE FROM OrderDetail od WHERE od.paymentStatus = 'READY'")
    void deleteAllReadyOrders();

    @Query("SELECT NEW rastle.dev.rastle_backend.domain.order.dto.OrderSimpleInfo(" +
        "o.id, o.createdTime, o.orderNumber, o.deliveryStatus) " +
        "FROM OrderDetail o WHERE o.member.id = :memberId AND o.paymentStatus = 'PAID' ORDER BY o.createdTime DESC")
    Page<OrderSimpleInfo> findSimpleOrderInfoByMemberId(@Param("memberId") Long memberId, Pageable pageable);
}
