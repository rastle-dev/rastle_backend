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
    String merchantUID;
    Long cancelRequestAmount;
    String reason;
    String refundHolder;
    String refundBank;
    String refundAccount;
}
