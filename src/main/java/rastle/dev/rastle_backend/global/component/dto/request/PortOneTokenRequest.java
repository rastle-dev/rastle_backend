package rastle.dev.rastle_backend.global.component.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortOneTokenRequest {
    String imp_key;
    String imp_secret;
}
