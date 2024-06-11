package rastle.dev.rastle_backend.domain.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rastle.dev.rastle_backend.domain.order.dto.request.ProductReturnRequest;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderReturnResponse {
    ProductReturnRequest[] productReturnRequests;
}
