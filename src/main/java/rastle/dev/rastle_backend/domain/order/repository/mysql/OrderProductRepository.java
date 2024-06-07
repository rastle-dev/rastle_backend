package rastle.dev.rastle_backend.domain.order.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.order.dto.SimpleProductOrderInfo;
import rastle.dev.rastle_backend.domain.order.model.OrderProduct;

import java.util.List;
import java.util.Optional;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    @Query("SELECT NEW rastle.dev.rastle_backend.domain.order.dto.SimpleProductOrderInfo(" +
        "op.product.mainThumbnailImage, op.product.id, op.productOrderNumber, op.product.name, op.color, op.size, op.count, op.price, op.totalPrice, op.orderStatus, op.cancelAmount, op.cancelRequestAmount, op.trackingNumber) " +
        "FROM OrderProduct op JOIN FETCH op.product WHERE op.orderDetail.id = :orderId")
    List<SimpleProductOrderInfo> findSimpleProductOrderInfoByOrderId(@Param("orderId") Long orderId);

    @Query("SELECT op FROM OrderProduct op JOIN FETCH op.orderDetail JOIN FETCH op.orderDetail.payment JOIN FETCH op.orderDetail.delivery WHERE op.productOrderNumber=:productOrderNumber")
    Optional<OrderProduct> findByProductOrderNumber(@Param("productOrderNumber") Long productOrderNumber);

    @Modifying
    @Query("UPDATE OrderProduct op SET op.trackingNumber=null WHERE op.productOrderNumber=:productOrderNumber")
    void deleteTrackingNumberByProductOrderNumber(@Param("productOrderNumber") Long productOrderNumber);

    List<OrderProduct> findByTrackingNumber(String trackingNumber);
    @Query("SELECT op.trackingNumber FROM OrderProduct op WHERE op.orderStatus = 'DELIVERY_STARTED'")
    List<String> findTrackingNumberOfNotDelivered();
}
