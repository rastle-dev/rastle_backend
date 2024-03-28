package rastle.dev.rastle_backend.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CancelOrderRequest {
    String impId;
    Long productOrderNumber;
    Long cancelAmount;
    Long cancelRequestId;
}
