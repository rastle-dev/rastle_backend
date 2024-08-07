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
        Boolean verified;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SelectedProductsDTO {
        private String title;
        private Long price;
        private String color;
        private String size;
        private Long count;
        private String mainThumbnailImage;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PaymentPrepareRequest {
        String merchant_uid;
        Long couponId;
        Long deliveryPrice;
        Long islandDeliveryPrice;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PaymentPrepareResponse {
        String merchant_uid;
        Long number;
    }
}
