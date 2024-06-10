package rastle.dev.rastle_backend.domain.order.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.order.model.OrderProduct;

import java.util.List;
import java.util.Optional;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    @Query("SELECT op FROM OrderProduct op WHERE op.deliveryTime != null AND op.orderStatus != rastle.dev.rastle_backend.global.common.enums.OrderStatus.COMPLETED")
    List<OrderProduct> findDeliveredOrders();

    @Query(value = "SELECT " +
        "p.main_thumbnail_image as thumbnailUrl, " +
        "p.product_id as productId, " +
        "od.order_number as orderNumber, " +
        "op.product_order_number as productOrderNumber, " +
        "p.name as name, " +
        "op.color as color, " +
        "op.size as size, " +
        "op.count as count, " +
        "op.price as price, " +
        "op.total_price as totalPrice, " +
        "op.order_status as status, " +
        "op.cancel_amount as cancelAmount, " +
        "op.cancel_request_amount as cancelRequestAmount, " +
        "op.tracking_number as trackingNumber " +
        "FROM order_product op " +
        "LEFT OUTER JOIN order_detail od ON op.order_detail_id = od.order_detail_id " +
        "LEFT OUTER JOIN product_base p ON op.product_id = p.product_id " +
        "WHERE op.order_detail_id = :orderId", nativeQuery = true)
    List<SimpleProductOrderInterface> findSimpleProductOrderInfoByOrderId(@Param("orderId") Long orderId);


    @Query(value = "SELECT " +
        "p.main_thumbnail_image as thumbnailUrl, " +
        "p.product_id as productId, " +
        "od.order_number as orderNumber, " +
        "op.product_order_number as productOrderNumber, " +
        "p.name as name, " +
        "op.color as color, " +
        "op.size as size, " +
        "op.count as count, " +
        "op.price as price, " +
        "op.total_price as totalPrice, " +
        "op.order_status as status, " +
        "op.cancel_amount as cancelAmount, " +
        "op.cancel_request_amount as cancelRequestAmount, " +
        "op.tracking_number as trackingNumber " +
        "FROM order_product op " +
        "LEFT OUTER JOIN product_base p ON op.product_id = p.product_id " +
        "LEFT OUTER JOIN order_detail od ON op.order_detail_id = od.order_detail_id " +
        "WHERE od.member_id = :memberId", nativeQuery = true)
    List<SimpleProductOrderInterface> findSimpleProductOrderInfoByMemberId(@Param("memberId") Long memberId);

    interface SimpleProductOrderInterface {
        String getThumbnailUrl();
        Long getProductId();
        Long getOrderNumber();
        Long getProductOrderNumber();
        String getName();
        String getColor();
        String getSize();
        Long getCount();
        Long getPrice();
        Long getTotalPrice();
        String getStatus();
        Long getCancelAmount();
        Long getCancelRequestAmount();
        String getTrackingNumber();
    }

    @Query("SELECT op FROM OrderProduct op JOIN FETCH op.orderDetail JOIN FETCH op.orderDetail.payment JOIN FETCH op.orderDetail.delivery WHERE op.productOrderNumber=:productOrderNumber")
    Optional<OrderProduct> findByProductOrderNumber(@Param("productOrderNumber") Long productOrderNumber);

    @Modifying
    @Query("UPDATE OrderProduct op SET op.trackingNumber=:trackingNumber, op.orderStatus=rastle.dev.rastle_backend.global.common.enums.OrderStatus.DELIVERY_READY WHERE op.productOrderNumber=:productOrderNumber")
    void updateOrderProductTrackingNumber(@Param("trackingNumber") String trackingNumber, @Param("productOrderNumber") Long productOrderNumber);

    @Modifying
    @Query("UPDATE OrderProduct op SET op.trackingNumber=null, op.orderStatus=rastle.dev.rastle_backend.global.common.enums.OrderStatus.PAID WHERE op.productOrderNumber=:productOrderNumber")
    void deleteTrackingNumberByProductOrderNumber(@Param("productOrderNumber") Long productOrderNumber);

    List<OrderProduct> findByTrackingNumber(String trackingNumber);

    @Query("SELECT op.trackingNumber FROM OrderProduct op WHERE op.orderStatus = 'DELIVERY_STARTED'")
    List<String> findTrackingNumberOfNotDelivered();
}
