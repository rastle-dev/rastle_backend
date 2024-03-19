package rastle.dev.rastle_backend.global.component.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PortOneTokenResponse {
    Long code;
    String message;
    PortOneAccessToken response;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PortOneAccessToken {
        String access_token;
        Long now;
        Long expired_at;
    }
}
