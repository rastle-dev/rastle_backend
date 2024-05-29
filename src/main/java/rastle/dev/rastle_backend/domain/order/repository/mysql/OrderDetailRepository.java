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
    Optional<OrderDetail> findByOrderNumber(@Param("orderNumber") Long orderNumber);

    @Modifying
    @Query("DELETE FROM OrderDetail od WHERE od.orderStatus = 'READY'")
    void deleteAllReadyOrders();
    @Query("SELECT od FROM OrderDetail od ")
    List<OrderDetail> findDeliveredOrders();

    @Query("SELECT NEW rastle.dev.rastle_backend.domain.order.dto.OrderSimpleInfo(" +
        "o.id, o.createdTime, o.orderNumber, o.orderStatus, o.orderStatus) " +
        "FROM OrderDetail o WHERE o.member.id = :memberId AND o.orderStatus != 'CREATED' ORDER BY o.createdTime DESC")
    Page<OrderSimpleInfo> findSimpleOrderInfoByMemberId(@Param("memberId") Long memberId, Pageable pageable);
}
