package rastle.dev.rastle_backend.domain.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class OrderCancelRequest {
    Long orderNumber;
    ProductOrderCancelRequest[] productOrderCancelRequests;
    String reason;
}
