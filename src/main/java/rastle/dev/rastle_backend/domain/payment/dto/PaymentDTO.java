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
        String pg;
        String pay_method;
        String merchant_uid;
        String name;
        Long amount;
        String buyer_email;
        String buyer_name;
        String buyer_tel;
        String buyer_addr;
        String buyer_postcode;
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
