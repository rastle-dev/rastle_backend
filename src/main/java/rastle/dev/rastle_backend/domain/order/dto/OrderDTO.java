package rastle.dev.rastle_backend.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class OrderDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderCreateRequest {

        List<ProductOrderRequest> orderProducts;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        private static class ProductOrderRequest {
            Long productId;
            String color;
            String size;
            Long count;
            Long totalPrice;
        }

    }


    @Getter
    @AllArgsConstructor
    @Builder
    public static class OrderCreateResponse {

    }
}
