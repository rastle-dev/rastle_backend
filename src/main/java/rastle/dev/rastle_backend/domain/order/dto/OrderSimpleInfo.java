package rastle.dev.rastle_backend.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
}
