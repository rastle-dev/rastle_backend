package rastle.dev.rastle_backend.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReturnOrderResponse {
    String impId;
    Long productOrderNumber;
    Long returnAmount;
}
