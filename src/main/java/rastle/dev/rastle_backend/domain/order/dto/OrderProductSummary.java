package rastle.dev.rastle_backend.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rastle.dev.rastle_backend.domain.order.dto.OrderDTO.ProductOrderResponse;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductSummary {
    Long orderPrice;
    List<ProductOrderResponse> productOrderResponses;
}
