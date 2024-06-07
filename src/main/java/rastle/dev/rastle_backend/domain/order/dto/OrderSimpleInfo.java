package rastle.dev.rastle_backend.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderDetailRepository.OrderSimpleInterface;
import rastle.dev.rastle_backend.global.common.enums.OrderStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderSimpleInfo {
    Long orderId;
    LocalDateTime orderDate;
    String orderNumber;
    OrderStatus orderStatus;
    OrderStatus deliveryStatus;

    public OrderSimpleInfo(Long orderId, LocalDateTime orderDate, Long orderNumber, OrderStatus orderStatus, OrderStatus deliveryStatus) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderNumber = orderNumber.toString();
        this.orderStatus = orderStatus;
        this.deliveryStatus = deliveryStatus;
    }

    public static OrderSimpleInfo fromInterface(OrderSimpleInterface orderSimpleInterface) {
        return new OrderSimpleInfo(
            orderSimpleInterface.getOrderId(),
            orderSimpleInterface.getOrderDate(),
            orderSimpleInterface.getOrderNumber(),
            OrderStatus.getFromStatus(orderSimpleInterface.getOrderStatus()),
            OrderStatus.getFromStatus(orderSimpleInterface.getDeliveryStatus())
        );
    }
}
