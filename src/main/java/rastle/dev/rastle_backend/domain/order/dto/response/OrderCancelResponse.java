package rastle.dev.rastle_backend.domain.order.dto.response;

import lombok.*;
import rastle.dev.rastle_backend.domain.order.dto.request.ProductOrderCancelRequest;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCancelResponse {
    ProductOrderCancelRequest[] cancelProductOrders;

}
