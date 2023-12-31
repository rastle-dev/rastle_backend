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
        @Schema(description = "상품 이름", defaultValue = "연그레이 와이드 팬츠")
        String name;
        @Schema(description = "상품 색상", defaultValue = "BLUE")
        String color;
        @Schema(description = "상품 사이즈", defaultValue = "M")
        String size;
        @Schema(description = "상품 구매 수량", defaultValue = "2")
        Long count;
        @Schema(description = "상품 총 구매 금액", defaultValue = "60000 // 상품 1개 금액 X 상품 구매 수량")
        Long totalPrice;

        public static ProductOrderResponse toResponse(ProductOrderRequest request, String productOrderNumber) {
            return ProductOrderResponse.builder()
                    .productId(request.productId)
                    .name(request.name)
                    .color(request.color)
                    .size(request.size)
                    .count(request.count)
                    .totalPrice(request.totalPrice)
                    .productOrderNumber(productOrderNumber)
                    .build();
        }

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductOrderResponse {
        @Schema(description = "상품 아이디", defaultValue = "1")
        Long productId;
        @Schema(description = "상품 이름", defaultValue = "연그레이 와이드 팬츠")
        String name;
        @Schema(description = "상품 색상", defaultValue = "BLUE")
        String color;
        @Schema(description = "상품 사이즈", defaultValue = "M")
        String size;
        @Schema(description = "상품 구매 수량", defaultValue = "2")
        Long count;
        @Schema(description = "상품 총 구매 금액", defaultValue = "60000 // 상품 1개 금액 X 상품 구매 수량")
        Long totalPrice;
        @Schema(description = "상품 주문번호", defaultValue = "123459")
        String productOrderNumber;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderCreateResponse {
        Long orderDetailId;
        @Schema(description = "주문 번호", defaultValue = "2123459")
        String orderNumber;

        List<ProductOrderResponse> orderProducts;
    }
}
