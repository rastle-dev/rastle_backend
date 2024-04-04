package rastle.dev.rastle_backend.domain.order.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.order.dto.SimpleProductOrderInfo;
import rastle.dev.rastle_backend.domain.order.model.OrderProduct;

import java.util.List;
import java.util.Optional;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    @Query("SELECT NEW rastle.dev.rastle_backend.domain.order.dto.SimpleProductOrderInfo(" +
        "p.mainThumbnailImage, p.id, op.productOrderNumber, p.name, op.color, op.size, op.count, op.price, op.totalPrice, op.orderStatus, op.cancelAmount) " +
        "FROM OrderProduct op LEFT OUTER JOIN ProductBase p ON op.product.id = p.id WHERE op.orderDetail.id = :orderId")
    List<SimpleProductOrderInfo> findSimpleProductOrderInfoByOrderId(@Param("orderId") Long orderId);

    @Query("SELECT op FROM OrderProduct op JOIN FETCH op.orderDetail JOIN FETCH op.orderDetail.payment WHERE op.productOrderNumber=:productOrderNumber")
    Optional<OrderProduct> findByProductOrderNumber(@Param("productOrderNumber") Long productOrderNumber);

    Optional<OrderProduct> findByTrackingNumber(String trackingNumber);
    @Query("SELECT op.trackingNumber FROM OrderProduct op WHERE op.orderStatus = 'DELIVERY_STARTED'")
    List<String> findTrackingNumberOfNotDelivered();
}
