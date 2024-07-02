package rastle.dev.rastle_backend.global.component.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PortOnePaymentCancelRequest {
    String merchant_uid;
    String imp_uid;
    long amount;
    long checksum;

    @Override
    public String toString() {
        return "PortOnePaymentCancelRequest{" +
            "merchant_uid='" + merchant_uid + '\'' +
            ", imp_uid='" + imp_uid + '\'' +
            ", amount=" + amount +
            ", checksum=" + checksum +
            '}';
    }
}
