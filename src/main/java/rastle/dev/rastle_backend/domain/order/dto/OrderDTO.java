package rastle.dev.rastle_backend.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

public class OrderDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderCreateRequest {

        List<ProductOrderRequest> orderProducts;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductOrderRequest {
        @Schema(description = "상품 아이디", defaultValue = "1")
        Long productId;
        @Schema(description = "상품 색상", defaultValue = "BLUE")
        String color;
        @Schema(description = "상품 사이즈", defaultValue = "M")
        String size;
        @Schema(description = "상품 구매 수량", defaultValue = "2")
        Long count;
        @Schema(description = "상품 총 구매 금액", defaultValue = "60000 // 상품 1개 금액 X 상품 구매 수량")
        Long totalPrice;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderCreateResponse {
        String productOrderId;
        @Schema(description = "주문 번호", defaultValue = "20231201T1010001")
        String orderNumber;

        List<ProductOrderRequest> orderProducts;
    }
}
