package rastle.dev.rastle_backend.global.component.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetDeliveryStatusRequest {
    String query;
    GetDeliveryStatusVariable variables;
    @Builder
    public GetDeliveryStatusRequest(String query, String carrierId, String trackingNumber) {
        this.query = query;
        this.variables = new GetDeliveryStatusVariable(carrierId, trackingNumber);
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    static class GetDeliveryStatusVariable {
        String carrierId;
        String trackingNumber;
    }
}
