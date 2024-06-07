package rastle.dev.rastle_backend.domain.order.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.order.model.OrderDetail;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    @Query("SELECT od FROM OrderDetail od JOIN FETCH od.orderProduct JOIN FETCH od.payment JOIN FETCH od.delivery JOIN FETCH od.member WHERE od.id = :orderId AND od.member.id = :memberId")
    Optional<OrderDetail> findByIdAndMemberId(@Param("orderId") Long orderId, @Param("memberId") Long memberId);

    List<OrderDetail> findByMemberId(Long memberId);

    @Query("SELECT od FROM OrderDetail od JOIN FETCH od.orderProduct JOIN FETCH od.member WHERE od.orderNumber = :orderNumber")
    Optional<OrderDetail> findByOrderNumber(@Param("orderNumber") Long orderNumber);

    @Query("SELECT od FROM OrderDetail od JOIN FETCH od.orderProduct JOIN FETCH od.payment JOIN FETCH od.delivery JOIN FETCH od.member WHERE od.orderNumber = :orderNumber AND od.member.id = :memberId")
    Optional<OrderDetail> findByOrderNumberAndMemberId(@Param("orderNumber") Long orderNumber, @Param("memberId") Long memberId);

    @Modifying
    @Query("DELETE FROM OrderDetail od WHERE od.orderStatus = 'READY'")
    void deleteAllReadyOrders();
    @Query("SELECT od FROM OrderDetail od ")
    List<OrderDetail> findDeliveredOrders();

    @Query(value = "SELECT " +
        "o.order_detail_id as orderId, " +
        "o.created_time as orderDate, " +
        "o.order_number as orderNumber, " +
        "o.order_status as orderStatus, " +
        "o.order_status as deliveryStatus " +
        "FROM order_detail o WHERE o.member_id = :memberId AND o.order_status != 'CREATED' ORDER BY o.created_time DESC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<OrderSimpleInterface> findSimpleOrderInfoByMemberId(@Param("memberId") Long memberId, @Param("limit") Long limit, @Param("offset") Long offset);

    interface OrderSimpleInterface {
        Long getOrderId();
        LocalDateTime getOrderDate();
        String getOrderNumber();
        String getOrderStatus();
        String getDeliveryStatus();
    }

    @Query("SELECT COUNT(od.id) FROM OrderDetail od WHERE od.member.id = :memberId GROUP BY od.member.id")
    Long countSimpleOrderInfoByMemberId(@Param("memberId") Long memberId);

}
