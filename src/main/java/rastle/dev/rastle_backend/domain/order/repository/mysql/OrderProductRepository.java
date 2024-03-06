package rastle.dev.rastle_backend.domain.order.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.order.dto.SimpleProductOrderInfo;
import rastle.dev.rastle_backend.domain.order.model.OrderProduct;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    @Query("SELECT NEW rastle.dev.rastle_backend.domain.order.dto.SimpleProductOrderInfo(" +
        "p.mainThumbnailImage, p.id, p.name, op.color, op.size, op.count, op.totalPrice) " +
        "FROM OrderProduct op LEFT OUTER JOIN ProductBase p ON op.product.id = p.id WHERE op.orderDetail.id = :orderId")
    List<SimpleProductOrderInfo> findSimpleProductOrderInfoByOrderId(@Param("orderId") Long orderId);

    @Query("SELECT CAST(COALESCE(SUM(op.totalPrice), 0) AS Long) " +
        "FROM OrderProduct op LEFT OUTER JOIN OrderDetail o ON op.orderDetail.id = o.id WHERE o.orderNumber = :orderNumber GROUP BY o.id")
    Long findOrderProductPriceSumByOrderNumber(@Param("orderNumber") String orderNumber);
}
