package rastle.dev.rastle_backend.domain.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductOrderCancelRequest {
    Long productOrderNumber;
    Long cancelAmount;
}
