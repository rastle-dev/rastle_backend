package rastle.dev.rastle_backend.domain.payment.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PortOneWebHookResponse {
    String status;
    String message;
}
