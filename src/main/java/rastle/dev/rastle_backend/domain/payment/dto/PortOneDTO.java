package rastle.dev.rastle_backend.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class PortOneDTO {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    public static class PortOnePaymentResponse {
        Long code;
        String message;
        PortOnePaymentResult response;


        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        @Setter
        public static class PortOnePaymentResult {
            Long amount;
            String apply_num;
            String bank_code;
            String bank_name;
            String buyer_addr;
            String buyer_email;
            String buyer_name;
            String buyer_postcode;
            String buyer_tel;
            String cancel_amount;
            List<CancelInfo> cancel_history;
            String cancel_reason;
            List<String> cancel_receipt_urls;
            Long cancelled_at;
            String card_code;
            String card_name;
            String card_number;
            String card_quota;
            String card_type;
            String cash_receipt_issued;
            String channel;
            String currency;
            String custom_data;
            String customer_uid;
            String customer_uid_usage;
            String emb_pg_provider;
            Boolean escrow;
            String fail_reason;
            Long failed_at;
            String imp_uid;
            String merchant_uid;
            Long paid_at;
            String pay_method;
            String pg_id;
            String pg_provider;
            String pg_tid;
            String receipt_url;
            Long started_at;
            String status;
            String user_agent;
            String vbank_code;
            Long vbank_date;
            String vbank_holder;
            String vbank_issued_at;
            String vbank_name;
            String vbank_num;


        }

        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        @Setter
        public static class CustomData {
            Long couponId;
            Long deliveryPrice;
            String deliveryMsg;
        }

        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        @Setter
        public static class CancelInfo {
            String pg_tid;
            Long amount;
            Long cancelled_at;
            String reason;
            String receipt_url;
            String cancellation_id;
        }
    }
}
