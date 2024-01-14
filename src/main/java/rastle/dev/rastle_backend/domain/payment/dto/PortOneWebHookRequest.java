package rastle.dev.rastle_backend.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PortOneWebHookRequest {
    String imp_uid;
    String merchant_uid;
    String status;
}
