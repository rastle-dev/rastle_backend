package rastle.dev.rastle_backend.global.component.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class WebHookRegisterRequest {
    String query;
    WebHookRegisterVariable variables;
    @Builder
    public WebHookRegisterRequest(String query, String carrierId, String trackingNumber, String callbackUrl, LocalDateTime expirationTime) {
        this.query = query;
        this.variables = new WebHookRegisterVariable(carrierId, trackingNumber, callbackUrl, expirationTime);
    }

}
