package rastle.dev.rastle_backend.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PaymentDTO {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PaymentVerificationRequest {
        String imp_uid;
        String merchant_uid;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PaymentVerificationResponse {
        String pg;
        String pay_method;
        String orderNumber;
        String name;
        Long amount;
        String buyerEmail;
        String buyerName;
        String buyerTel;
        String buyerAddr;
        String buyerPostcode;
    }
}
