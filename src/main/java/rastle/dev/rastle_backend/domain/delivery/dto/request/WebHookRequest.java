package rastle.dev.rastle_backend.domain.delivery.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WebHookRequest {
     private String carrierId;
    private String trackingNumber;
}
