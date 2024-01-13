package rastle.dev.rastle_backend.global.component.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PortOnePaymentRequest {
    String merchant_uid;
    Long amount;
}
