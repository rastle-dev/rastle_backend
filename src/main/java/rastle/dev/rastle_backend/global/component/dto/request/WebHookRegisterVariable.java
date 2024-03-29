package rastle.dev.rastle_backend.global.component.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class WebHookRegisterVariable {
    RegisterInput input;
    @Builder
    public WebHookRegisterVariable(String carrierId, String trackingNumber, String callbackUrl, LocalDateTime expirationTime) {
        this.input = new RegisterInput(carrierId, trackingNumber, callbackUrl, expirationTime);
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegisterInput {
        String carrierId;
        String trackingNumber;
        String callbackUrl;
        LocalDateTime expirationTime;
    }
}
