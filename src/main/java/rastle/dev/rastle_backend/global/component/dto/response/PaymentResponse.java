package rastle.dev.rastle_backend.global.component.dto.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import rastle.dev.rastle_backend.global.component.dto.PortOneDTO.PortOnePaymentResponse.CancelInfo;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@AllArgsConstructor
public class PaymentResponse {
    @Getter
    private final Map<String, Object> map;
    private final ObjectMapper objectMapper;

    public String getDeliveryMsg() {

        Map<String, Object> customData = null;
        try {
            customData = objectMapper.readValue((String) map.getOrDefault("custom_data", ""), new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return (String) customData.getOrDefault("deliveryMsg", "");
    }

    public Long getDeliveryPrice() {
        Map<String, Object> customData = null;
        try {
            customData = objectMapper.readValue((String) map.getOrDefault("custom_data", ""), new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return Long.valueOf((Integer) customData.getOrDefault("deliveryPrice", 3000));
    }

    public Long getCouponId() {
        Map<String, Object> customData = null;
        try {
            customData = objectMapper.readValue((String) map.getOrDefault("custom_data", ""), new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return Long.valueOf((Integer) customData.getOrDefault("couponId", null));
    }

    public String getVbankNum() {
        return (String) map.getOrDefault("vbank_num", "");
    }

    public String getVbankName() {
        return (String) map.getOrDefault("vbank_name", "");
    }

    public LocalDateTime getVbankIssuedAt() {
        return Instant.ofEpochSecond(Long.valueOf((Integer) map.getOrDefault("vbank_issued_at", 0))).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }

    public String getVbankHolder() {
        return (String) map.getOrDefault("vbank_holder", "");
    }

    public LocalDateTime getVbankDate() {
        return Instant.ofEpochSecond(Long.valueOf((Integer) map.getOrDefault("vbank_date", 0))).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();

    }

    public String getVbankCode() {
        return (String) map.getOrDefault("vbank_code", "");
    }

    public String getUserAgent() {
        return (String) map.getOrDefault("user_agent", "");
    }

    public String getStatus() {
        return (String) map.getOrDefault("status", "");
    }

    public LocalDateTime getStartedAt() {
        return Instant.ofEpochSecond(Long.valueOf((Integer) map.getOrDefault("started_at", 0))).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();

    }

    public String getReceiptUrl() {
        return (String) map.getOrDefault("receipt_url", "");
    }

    public String getPgTID() {
        return (String) map.getOrDefault("pg_tid", "");
    }

    public String getPgProvider() {
        return (String) map.getOrDefault(" pg_provider", "");
    }

    public String getPgId() {
        return (String) map.getOrDefault("pg_id", "");
    }

    public String getPayMethod() {
        return (String) map.getOrDefault("pay_method", "");
    }

    public LocalDateTime getPaidAt() {
        return Instant.ofEpochSecond(Long.valueOf((Integer) map.getOrDefault("paid_at", 0))).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();

    }

    public String getMerchantUID() {
        return (String) map.getOrDefault("merchant_uid", "");
    }

    public String getImpUID() {
        return (String) map.getOrDefault("imp_uid", "");
    }

    public LocalDateTime getFailedAt() {
        return Instant.ofEpochSecond(Long.valueOf((Integer) map.getOrDefault("failed_at", 0))).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }

    public String getFailReason() {
        return (String) map.getOrDefault("fail_reason", "");
    }

    public Boolean getEscrow() {
        return (Boolean) map.getOrDefault("escrow", false);
    }

    public String getEmbPgProvider() {
        return (String) map.getOrDefault("emb_pg_provider", "");
    }

    public String getCustomerUIDUsage() {
        return (String) map.getOrDefault("customer_uid_usage", "");
    }

    public String getCustomerUID() {
        return (String) map.getOrDefault("customer_uid", "");
    }


    public String getCurrency() {
        return (String) map.getOrDefault("currency", "");
    }

    public String getChannel() {
        return (String) map.getOrDefault("channel", "");
    }

    public String getCashReceiptIssued() {
        return (String) map.getOrDefault("cash_receipt_issued", "");
    }

    public String getCardType() {
        return (String) map.getOrDefault("card_type", "");
    }

    public Long getCardQuota() {
        return Long.valueOf((Integer) map.getOrDefault("card_quota", 0));
    }

    public String getCardNumber() {
        return (String) map.getOrDefault("card_number", "");
    }

    public String getCardName() {
        return (String) map.getOrDefault("card_name", "");
    }

    public String getCardCode() {
        return (String) map.getOrDefault("card_code", "");
    }

    public LocalDateTime getCancelledAt() {
        return Instant.ofEpochSecond(Long.valueOf((Integer) map.getOrDefault("cancelled_at", 0))).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }

    public List<String> getCancelReceiptUrls() {
        return (List<String>) map.getOrDefault("cancel_receipt_urls", new ArrayList<>());
    }

    public String getCancelReason() {
        return (String) map.getOrDefault("cancel_reason", "");
    }

    public List<CancelInfo> getCancelHistory() {
        return (List<CancelInfo>) map.getOrDefault("cancel_history", new ArrayList<>());
    }

    public Long getCancelAmount() {
        return Long.valueOf((Integer) map.getOrDefault("cancel_amount", 0));
    }

    public String getBuyerTel() {
        return (String) map.getOrDefault("buyer_tel", "");
    }

    public String getBuyerPostCode() {
        return (String) map.getOrDefault("buyer_postcode", "");
    }

    public String getBuyerName() {
        return (String) map.getOrDefault("buyer_name", "");
    }

    public String getBuyerEmail() {
        return (String) map.getOrDefault("buyer_email", "");
    }

    public String getBuyerAddress() {
        return (String) map.getOrDefault("buyer_addr", "");
    }

    public String getBankName() {
        return (String) map.getOrDefault("bank_name", "");
    }

    public String getBankCode() {
        return (String) map.getOrDefault("bank_code", "");
    }

    public String getApplyNum() {
        return (String) map.getOrDefault("apply_num", "");
    }

    public Long getAmount() {
        return Long.valueOf((Integer) map.getOrDefault("amount", 0));
    }


}
