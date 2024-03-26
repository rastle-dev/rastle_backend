package rastle.dev.rastle_backend.domain.order.dto.response;

import lombok.*;
import rastle.dev.rastle_backend.global.common.enums.PaymentStatus;
import rastle.dev.rastle_backend.global.component.dto.PortOneDTO.PortOnePaymentResponse.CancelInfo;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCancelResponse {
    Long[] cancelProductOrderNumber;

}
